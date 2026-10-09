package app.gmaaps.profile.ai

import app.gmaaps.profile.ai.OutlierDetector.AnomalyResult

/**
 * Интерфейс для взаимодействия AI-анализа.
 * 
 * Определяет методы для вызова AI API из AiProfilePlugin и обработки ответов.
 */
interface AiApiClient {
    /**
     * Генерация рекомендации на основе анализа профиля через AI API.
     * 
     * @param apiConfig конфигурация (API URL, ключ, агрессивность)
     * @param analysisResult агрегированные данные глюкозы/bolus/carbs
     * @param anomalies результаты обнаружения аномалий
     * @return рекомендация по изменению профиля или null если изменений не требуется
     */
    suspend fun generateRecommendation(
        apiConfig: AiProfileConfig,
        analysisResult: AnalysisResult,
        anomalies: AnomalyResult
    ): ProfileRecommendation

    /**
     * Отправить запрос на анализ и получить рекомендацию.
     */
    suspend fun sendAnalysisRequest(
        config: AiProfileConfig,
        analysisResult: AnalysisResult
    ): ProfileRecommendation?

    /**
     * Проверить подключение к AI API.
     */
    suspend fun testConnection(config: AiProfileConfig): TestResult
}

/**
 * Результат проверки подключения к AI API.
 */
sealed class TestResult {
    data class Success(val isConnected: Boolean, val message: String) : TestResult()
    data class Failure(val errorMessage: String) : TestResult()
}

