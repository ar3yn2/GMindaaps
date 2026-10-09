package app.gmaaps.profile.ai
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
/**
 * ?????????? AiApiClient ? ?????????????? Ktor HTTP ???????.
 */
class AiHttpClient(
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
) : AiApiClient {
    override suspend fun generateRecommendation(
        apiConfig: AiProfileConfig,
        analysisResult: AnalysisResult,
        anomalies: app.gmaaps.profile.ai.OutlierDetector.AnomalyResult
    ): ProfileRecommendation {
        return try {
            // ????????? JSON payload ??? AI API
            val payload = AiApiRequest(
                config = apiConfig,
                analysisJson = analysisResult.toJsonPayload(),
                anomaliesSummary = AiAnomaliesSummary(
                    totalSpikes = anomalies.totalSpikesCount,
                    unexplainedSpikes = anomalies.unexplainedSpikesCount,
                    anomalyType = anomalies.anomalyType.name,
                    daysWithAnomalies = anomalies.daysWithAnomalies
                )
            )
            // ?????????? ?????? ? AI API
            val response = httpClient.post(apiConfig.apiUrl) {
                header("Authorization", "Bearer ${apiConfig.apiKey}")
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
            if (response.status.value !in 200..299) {
                return ProfileRecommendation.defaultNoChange()
            }
            // ?????? ?????
            val aiResponse = response.body<AiApiResponse>()
            
            // ??????? ???????????? ?? ?????? AI
            ProfileRecommendation(
                isfShiftPercent = aiResponse.isfShiftPercent ?: 0f,
                basalShiftPercent = aiResponse.basalShiftPercent ?: 0f,
                confidence = (aiResponse.confidence ?: 0.5f).coerceIn(0f, 1f),
                reason = aiResponse.reason ?: "AI analysis complete"
            )
        } catch (e: Exception) {
            // ? ?????? ?????? ?????????? ???????????? ?? ?????????
            ProfileRecommendation.defaultNoChange()
        }
    }
    override suspend fun sendAnalysisRequest(
        config: AiProfileConfig,
        analysisResult: AnalysisResult
    ): ProfileRecommendation? {
        return try {
            generateRecommendation(config, analysisResult, OutlierDetector.AnomalyResult.noAnomalies())
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun testConnection(config: AiProfileConfig): TestResult {
        return try {
            val response = httpClient.get(config.apiUrl) {
                header("Authorization", "Bearer ${config.apiKey}")
            }
            
            if (response.status.value in 200..299) {
                TestResult.Success(true, "AI API connection successful")
            } else {
                TestResult.Failure("HTTP ${response.status.value}: ${response.status.description}")
            }
        } catch (e: Exception) {
            TestResult.Failure("Connection failed: ${e.message}")
        }
    }
    /**
     * JSON ????????? ??????? ? AI API.
     */
    @Serializable
    private data class AiApiRequest(
        val config: AiProfileConfig,
        val analysisJson: String,
        val anomaliesSummary: AiAnomaliesSummary
    )
    @Serializable
    private data class AiAnomaliesSummary(
        val totalSpikes: Int,
        val unexplainedSpikes: Int,
        val anomalyType: String,
        val daysWithAnomalies: Int
    )
    /**
     * JSON ????????? ?????? ?? AI API.
     */
    @Serializable
    private data class AiApiResponse(
        val isfShiftPercent: Float?,
        val basalShiftPercent: Float?,
        val confidence: Float?,
        val reason: String?
    )
}

