package app.gmaaps.profile.ai

import app.gmaaps.core.data.model.BS
import app.gmaaps.core.data.model.CA
import app.gmaaps.core.data.model.GV
import kotlin.math.abs

/**
 * Информация о выбросе (spike) глюкозы.
 * 
 * Используется для определения, был ли скачок BG объяснён приёмом углеводов/болюсом,
 * или это необъяснимый выброс (болезнь, ошибка CGM и т.д.).
 */
data class SpikeInfo(
    /** Timestamp момента пика глюкозы в миллисекундах (epoch) */
    val timestamp: Long,
    
    /** Значение глюкозы при пике в mg/dL */
    val bgValue: Double,
    
    /** Предыдущее значение до скачка — для расчёта абсолютного изменения */
    val previousBg: Double?,
    
    /** Насколько сильно вырос BG (разница с предыдущим чтением) */
    val spikeDelta: Double,
    
    /** Есть ли соответствующий болюс в пределах lookback-окна? */
    var hasCorrespondingBolus: Boolean = false,
    
    /** Есть ли соответствующие углеводы в пределах lookback-окна? */
    var hasCorrespondingCarbs: Boolean = false,
    
    /** Время обнаружения (для логирования) */
    val detectedAt: Long = System.currentTimeMillis()
) {
    /** Общий флаг — объяснён ли этот скачок */
    fun isExplained(): Boolean = hasCorrespondingBolus || hasCorrespondingCarbs

    companion object {
        private const val LOOKBACK_WINDOW_MS = 90L * 60 * 1000L // 90 минут в миллисекундах
        
        /**
         * Создать список SpikeInfo из данных CGM с отметками о соответствующих bolus/carbs.
         */
        fun detectFromReadings(
            bgReadings: List<GV>,
            boluses: List<BS>,
            carbs: List<CA>,
            spikeThreshold: Double, // минимальный скачок в mg/dL
            maxLookbackMinutes: Long = 90L
        ): List<SpikeInfo> {
            if (bgReadings.size < 2) return emptyList()

            val lookbackWindowMs = maxLookbackMinutes * 60 * 1000L
            
            // Сортируем по времени
            val sortedBg = bgReadings.sortedBy { it.timestamp }
            
            val spikes = mutableListOf<SpikeInfo>()
            
            for (i in 1 until sortedBg.size) {
                val current = sortedBg[i]
                val previous = sortedBg[i - 1]
                
                // Проверяем скачок (разница > threshold и рост вверх)
                val delta = current.value - previous.value
                if (delta >= spikeThreshold && current.value > previous.value) {
                    val spikeInfo = SpikeInfo(
                        timestamp = current.timestamp,
                        bgValue = current.value,
                        previousBg = previous.value,
                        spikeDelta = delta
                    )

                    // Ищем bolus в окне [timestamp - lookbackWindowMs .. timestamp + 30min]
                    val bolusEndWindow = current.timestamp + 30 * 60 * 1000L
                    val bolusStartWindow = current.timestamp - maxLookbackMinutes * 60 * 1000L
                    spikeInfo.hasCorrespondingBolus = boluses.any { bs ->
                        bs.timestamp in (bolusStartWindow..bolusEndWindow) && 
                        bs.amount > 0.5 // минимум 0.5U считается значимым
                    }

                    // Ищем carbs в окне [timestamp - lookbackMinutes .. timestamp + 120min]
                    val carbEndWindow = current.timestamp + 120 * 60 * 1000L
                    spikeInfo.hasCorrespondingCarbs = carbs.any { ca ->
                        ca.timestamp in (bolusStartWindow..carbEndWindow) && 
                        ca.amount > 3.0 // минимум 3г углеводов считается значимым
                    }

                    spikes.add(spikeInfo)
                }
            }
            
            return spikes
        }
    }
}

