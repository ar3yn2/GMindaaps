package app.aaps.profile.ai

import app.aaps.core.data.model.BS
import app.aaps.core.data.model.CA
import app.aaps.core.data.model.GV
import app.aaps.core.interfaces.db.PersistenceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

/**
 * Детектор аномалий и выбросов глюкозы.
 * 
 * Определяет, когда скачки BG вызваны реальными причинами (пропущенный bolus/карб),
 * а когда это случайные всплески (болезнь, ошибка CGM).
 * Если слишком много необъяснимых выбросов - AI не предлагаем корректировку.
 */
class OutlierDetector(
    private val persistence: PersistenceLayer
) {
    
    /**
     * Проверить данные на аномалии за указанный период.
     * 
     * @param config конфигурация AI (пороги, временные окна)
     * @return результат детекции аномалий
     */
    suspend fun detectAnomalies(config: AiProfileConfig): AnomalyResult {
        return withContext(Dispatchers.IO) {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - (config.analysisDays.toLong() * 24L * 60 * 60 * 1000)

            // Получаем данные
            val bgReadings = persistence.getBgReadingsDataFromTimeToTime(startTime, endTime, false)
                .filter { it.isValid && it.value > 20 && it.value < 600 }
            
            val boluses = persistence.getBolusesFromTime(startTime, true)
                .filter { it.isValid && it.amount > 1.0 } // только значимые bolus
            
            val carbs = persistence.getCarbsFromTimeExpanded(startTime, endTime, true)
                .filter { it.isValid && it.amount > 5.0 } // только значимые carb'ы

            // Детектим выбросы
            val spikeInfoList = SpikeInfo.detectFromReadings(
                bgReadings = bgReadings,
                boluses = boluses,
                carbs = carbs,
                spikeThreshold = config.spikeThreshold.toDouble(),
                maxLookbackMinutes = config.spikeLookbackMinutes.toLong()
            )

            // Группируем по сутам для проверки maxSpikesPerDay
            val spikesByDay = groupSpikesByDay(spikeInfoList)
            
            val daysWithTooManyUnexplainedSpikes = spikesByDay.count { daySpikes ->
                val unexplainedCount = daySpikes.count { !it.isExplained() }
                unexplainedCount > config.maxSpikesPerDay
            }

            // Определяем тип аномалии
            val anomalyType = when {
                daysWithTooManyUnexplainedSpikes >= 3 -> AnomalyType.HEAVY_ANOMALIES
                daysWithTooManyUnexplainedSpikes >= 1 -> AnomalyType.MODERATE_ANOMALIES
                else -> AnomalyType.NORMAL
            }

            // Проверяем покрытие CGM данными (должно быть >80%)
            val totalMinutes = if (bgReadings.isNotEmpty()) {
                (bgReadings.last().timestamp - bgReadings.first().timestamp) / (60 * 1000.0)
            } else 0.0
            
            val expectedReadings = totalMinutes / 5.0 // ~1 чтение каждые 5 мин
            val cgmCoverage = if (expectedReadings > 0) {
                (bgReadings.size / expectedReadings).toFloat()
            } else 0f

            AnomalyResult(
                anomalyType = anomalyType,
                totalSpikesCount = spikeInfoList.size,
                unexplainedSpikesCount = spikeInfoList.count { !it.isExplained() },
                explainedSpikesCount = spikeInfoList.count { it.isExplained() },
                daysWithAnomalies = daysWithTooManyUnexplainedSpikes,
                analysisDays = config.analysisDays,
                cgmCoveragePercent = cgmCoverage,
                isSufficientData = bgReadings.isNotEmpty() && 
                                   cgmCoverage >= 0.8f && 
                                   config.analysisDays >= config.minDataDays
            )
        }
    }

    /**
     * Сгруппировать выбросы по дням для проверки maxSpikesPerDay.
     */
    private fun groupSpikesByDay(spikes: List<SpikeInfo>): List<List<SpikeInfo>> {
        if (spikes.isEmpty()) return emptyList()
        
        // Получаем мин/макс timestamp из данных
        val minTs = spikes.minOf { it.timestamp }
        val maxTs = spikes.maxOf { it.timestamp }

        // Группируем по дням (86400000 ms в дне)
        val daySize = 24L * 60 * 60 * 1000L
        val groups = mutableMapOf<Long, MutableList<SpikeInfo>>()

        for (spike in spikes) {
            // Определяем "день" по timestamp
            val dayKey = ((spike.timestamp - minTs) / daySize).toLong() * daySize + minTs
            
            if (!groups.containsKey(dayKey)) {
                groups[dayKey] = mutableListOf()
            }
            groups[dayKey]!!.add(spike)
        }

        return groups.values.toList()
    }

    /**
     * Результат детекции аномалий.
     */
    data class AnomalyResult(
        /** Тип найденных аномалий */
        val anomalyType: AnomalyType,
        
        /** Общее количество выбросов BG (>30 mg/dL скачок) за период */
        val totalSpikesCount: Int,
        
        /** Количество необъяснимых выбросов (без bolus/carbs) */
        val unexplainedSpikesCount: Int,
        
        /** Количество объяснённых выбросов (были bolus/carbs) */
        val explainedSpikesCount: Int,
        
        /** Количество дней с аномалиями (>maxSpikesPerDay необъяснимых скачков в день) */
        val daysWithAnomalies: Int,
        
        /** Дней анализа */
        val analysisDays: Int,

        /** Покрытие CGM данными (должно быть >80%) */
        val cgmCoveragePercent: Float,

        /** Достаточно ли данных для анализа */
        val isSufficientData: Boolean
    ) {
        enum class AnomalyType {
            /** Аномалий нет — данные чистые, можно анализировать */
            NORMAL,
            
            /** Умеренные аномалии (1 день с выбросами) — анализировать с осторожностью */
            MODERATE_ANOMALIES,
            
            /** Много аномалий (>3 дней) — вероятно болезнь/ошибка CGM, не предлагать коррекцию */
            HEAVY_ANOMALIES
        }

        /** Проверить что можно предложить AI-коррекцию профиля */
        fun canRecommend(): Boolean {
            return isSufficientData && anomalyType != AnomalyType.HEAVY_ANOMALIES
        }

        /** Получить сообщение для пользователя о типе аномалии */
        fun getUserMessage(): String? {
            return when (anomalyType) {
                AnomalyType.NORMAL -> null // нет аномалий, всё хорошо
                AnomalyType.MODERATE_ANOMALIES -> 
                    "Обнаружены умеренные аномалии glucose. Анализ проведён с осторожностью."
                AnomalyType.HEAVY_ANOMALIES -> 
                    "Много необъяснимых скачков glucose (). " +
                    "Вероятно болезнь или ошибка CGM. Коррекция профиля отложена."
            }
        }
    }

    /**
     * Быстрая проверка: есть ли критичные аномалии за последние 24ч (для быстрого предупреждения).
     */
    suspend fun hasRecentAnomalies(config: AiProfileConfig): Boolean {
        return withContext(Dispatchers.IO) {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - (24L * 60 * 60 * 1000)

            val bgReadings = persistence.getBgReadingsDataFromTimeToTime(startTime, endTime, false)
                .filter { it.isValid && it.value > 20 && it.value < 600 }

            if (bgReadings.size < 5) return@withContext true // недостаточно данных

            val boluses = persistence.getBolusesFromTime(startTime, true)
                .filter { it.isValid && it.amount > 1.0 }

            val carbs = persistence.getCarbsFromTimeExpanded(startTime, endTime, true)
                .filter { it.isValid && it.amount > 5.0 }

            val spikes = SpikeInfo.detectFromReadings(
                bgReadings, boluses, carbs, 
                config.spikeThreshold.toDouble(), 
                config.spikeLookbackMinutes.toLong()
            )

            val unexplainedCount = spikes.count { !it.isExplained() }
            unexplainedCount > config.maxSpikesPerDay
        }
    }
}
