package app.aaps.profile.ai

import app.aaps.core.data.model.BS
import app.aaps.core.data.model.CA
import app.aaps.core.data.model.GV
import app.aaps.core.interfaces.db.PersistenceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Основной сервис агрегации данных для AI-анализа профиля.
 * 
 * Собирает данные из БД (глюкоза, bolus, carbs), рассчитывает метрики:
 * - среднюю глюкозу, медиану, стандартное отклонение, CV%
 * - TIR/TAR/TBR (время в/выше/ниже диапазона)
 * - количество и объём bolus/carbs за период
 * - статистку выбросов BG
 * 
 * Использует PersistenceLayer для чтения данных.
 */
class DataAggregator(
    private val persistence: PersistenceLayer
) {
    
    /**
     * Собрать все необходимые данные для анализа профиля.
     * 
     * @param config конфигурация AI (период, пороги и т.д.)
     * @return результат агрегации AnalysisResult
     */
    suspend fun collectAnalysisData(config: AiProfileConfig): AnalysisResult {
        return withContext(Dispatchers.IO) {
            // Определяем временной диапазон анализа
            val endTime = System.currentTimeMillis()
            val startTime = endTime - (config.analysisDays.toLong() * 24L * 60 * 60 * 1000)
            val periodMs = endTime - startTime

            // Получаем данные из БД
            val bgReadings = persistence.getBgReadingsDataFromTimeToTime(startTime, endTime, false)
                .filter { it.isValid && it.value > 20 && it.value < 600 } // фильтруем артефакты
            
            val boluses = persistence.getBolusesFromTime(startTime, true)
                .filter { it.isValid && it.amount > 0 }
            
            val carbs = persistence.getCarbsFromTimeExpanded(startTime, endTime, true)
                .filter { it.isValid && it.amount > 0 }

            // Рассчитываем метрики
            calculateAnalysisResult(
                bgReadings = bgReadings,
                boluses = boluses,
                carbs = carbs,
                startTime = startTime,
                endTime = endTime,
                analysisDays = config.analysisDays,
                periodMs = periodMs,
                spikeThreshold = config.spikeThreshold.toDouble(),
                maxLookbackMinutes = config.spikeLookbackMinutes.toLong()
            )
        }
    }

    /**
     * Рассчитать все метрики анализа из собранных данных.
     */
    private fun calculateAnalysisResult(
        bgReadings: List<GV>,
        boluses: List<BS>,
        carbs: List<CA>,
        startTime: Long,
        endTime: Long,
        analysisDays: Int,
        periodMs: Long,
        spikeThreshold: Double,
        maxLookbackMinutes: Long
    ): AnalysisResult {

        val totalBgReadingsCount = bgReadings.size
        
        // ==================== Глюкоза ====================
        val glucoseValues = bgReadings.map { it.value }
        
        val avgGlucose = if (glucoseValues.isNotEmpty()) {
            glucoseValues.average()
        } else 0.0

        val medianGlucose = if (glucoseValues.size > 1) {
            val sorted = glucoseValues.sorted()
            val mid = sorted.size / 2
            when {
                sorted.size % 2 == 0 -> (sorted[mid - 1] + sorted[mid]) / 2.0
                else -> sorted[mid].toDouble()
            }
        } else avgGlucose

        val stdDev = if (glucoseValues.size > 1) {
            val mean = glucoseValues.average()
            sqrt(glucoseValues.map { (it - mean).pow(2) }.average())
        } else 0.0

        // CV% — coefficient of variation (показатель стабильности глюкозы)
        val cvPercent = if (avgGlucose > 0 && stdDev > 0) {
            (stdDev / avgGlucose) * 100.0
        } else 0.0

        // ==================== TIR/TAR/TBR ====================
        val tirThresholds = Pair(70, 180) // mg/dL — стандартный целевой диапазон
        val tarCount = glucoseValues.count { it > tirThresholds.second }.toFloat()
        val tbrCount = glucoseValues.count { it < tirThresholds.first }.toFloat()

        val totalMinutes = periodMs / (60 * 1000.0) // общее время в минутах
        
        // Приблизительно считаем что чтения каждые ~5 минут
        val tirPercent = if (totalBgReadingsCount > 0 && totalMinutes > 0) {
            ((totalMinutes - tarCount * 5 - tbrCount * 5) / totalMinutes * 100).coerceIn(0f, 100f)
        } else 0f

        val tbrPercent = if (totalMinutes > 0) {
            (tbrCount * 5 / totalMinutes * 100).toFloat()
        } else 0f

        val tarPercent = if (totalMinutes > 0) {
            (tarCount * 5 / totalMinutes * 100).toFloat()
        } else 0f

        // ==================== Bolus и Carbs статистика ====================
        val totalBoluses = boluses.size
        val totalBolusUnits = boluses.sumOf { it.amount }
        val totalCarbsGrams = carbs.sumOf { it.amount }
        
        // Среднее углеводов на приём (если есть данные о приёмах пищи)
        // Углеводы обычно поступают в течение 2 часов после еды
        val mealCarbs = carbs.filter { 
            val hoursSince = (System.currentTimeMillis() - it.timestamp) / (1000.0 * 60 * 60)
            hoursSince < 48 // последние 48 часов — считаем приёмы пищи
        }
        val avgCarbsPerMeal = if (mealCarbs.isNotEmpty()) {
            mealCarbs.average { it.amount }
        } else 0.0

        // ==================== Выбросы BG ====================
        val spikeInfoList = SpikeInfo.detectFromReadings(
            bgReadings = bgReadings,
            boluses = boluses,
            carbs = carbs,
            spikeThreshold = spikeThreshold,
            maxLookbackMinutes = maxLookbackMinutes
        )

        val totalSpikesCount = spikeInfoList.size
        val unexplainedSpikesCount = spikeInfoList.count { !it.isExplained() }

        // Макс/мин глюкозы
        val maxGlucose = glucoseValues.maxOrNull() ?: 0.0
        val minGlucose = glucoseValues.minOrNull() ?: 0.0

        // ==================== Покрытие CGM данными ====================
        // Процент времени с работающим CGM (должно быть >80% по рекомендациям ADA)
        val cgmCoveragePercent = if (totalMinutes > 0 && totalBgReadingsCount > 0) {
            val expectedReadings = totalMinutes / 5.0 // ~1 чтение каждые 5 мин
            ((totalBgReadingsCount / expectedReadings) * 100).coerceIn(0f, 100f)
        } else 0f

        // ==================== Прогнозы ====================
        // ADAG formula: A1C ≈ (avgGlucose + 46.7) / 28.7
        val estimatedA1c = if (avgGlucose > 0) {
            (avgGlucose + 46.7) / 28.7
        } else null

        // ==================== Тренд последних 24ч ====================
        val recentTrend = calculateRecentTrend(bgReadings, endTime)

        return AnalysisResult(
            analysisPeriodMs = periodMs,
            analysisDays = analysisDays,
            avgGlucose = avgGlucose,
            medianGlucose = medianGlucose,
            glucoseStdDev = stdDev,
            cvPercent = cvPercent,
            tirPercent = tirPercent,
            tbrPercent = tbrPercent,
            tarPercent = tarPercent,
            totalBoluses = totalBoluses,
            totalBolusUnits = totalBolusUnits,
            totalCarbsGrams = totalCarbsGrams,
            avgCarbsPerMeal = avgCarbsPerMeal,
            unexplainedSpikesCount = unexplainedSpikesCount,
            totalSpikesCount = totalSpikesCount,
            maxGlucose = maxGlucose,
            minGlucose = minGlucose,
            cgmCoveragePercent = cgmCoveragePercent,
            totalBgReadingsCount = totalBgReadingsCount,
            estimatedA1c = estimatedA1c,
            recentTrend = recentTrend
        )
    }

    /**
     * Рассчитать направление тренда глюкозы за последние 24 часа.
     * 
     * Сравнивает среднюю глюкозу последних 6ч с предыдущими 6ч.
     */
    private fun calculateRecentTrend(bgReadings: List<GV>, endTime: Long): AnalysisResult.TrendDirection {
        if (bgReadings.size < 10) return AnalysisResult.TrendDirection.STABLE

        val now = System.currentTimeMillis()
        val last6h = now - 6L * 60 * 60 * 1000
        val prev6h = last6h - 6L * 60 * 60 * 1000

        val recentReadings = bgReadings.filter { it.timestamp > last6h }.map { it.value }
        val previousReadings = bgReadings.filter { 
            it.timestamp in (prev6h..last6h) && it.isValid 
        }.map { it.value }

        if (recentReadings.size < 3 || previousReadings.size < 3) return AnalysisResult.TrendDirection.STABLE

        val recentAvg = recentReadings.average()
        val previousAvg = previousReadings.average()

        // Разница >10 mg/dL за 6ч считается значимым трендом
        val diff = recentAvg - previousAvg
        return when {
            diff > 10 -> AnalysisResult.TrendDirection.UP
            diff < -10 -> AnalysisResult.TrendDirection.DOWN
            else -> AnalysisResult.TrendDirection.STABLE
        }
    }

    /**
     * Проверить что данные собраны и достаточно качественны для AI-анализа.
     */
    fun isDataSufficient(
        bgReadings: List<GV>,
        analysisDays: Int,
        minCoveragePercent: Float,
        minDataDays: Int
    ): Boolean {
        val totalMinutes = if (bgReadings.isNotEmpty()) {
            (bgReadings.last().timestamp - bgReadings.first().timestamp) / (60 * 1000.0)
        } else 0.0

        return bgReadings.isNotEmpty() &&
               analysisDays >= minDataDays &&
               totalMinutes > (minDataDays * 24 * 60) // минимум N дней данных
    }
}
