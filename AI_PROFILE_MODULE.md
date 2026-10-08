# AndroidAPS AI Profile Module — Overview

## What is it?

The **AI Profile Analyzer** module is an AndroidAPS plugin that automatically analyzes user data (CGM glucose, bolus, carbs) and proposes insulin profile adjustments (ISF, basal rate), using an external AI server.

User enters their API URL + key → module collects data → sends to AI → gets recommendation → validates with machine algorithm → shows notification with Apply/Reject buttons.

---

## Module Architecture

`
+-------------------------------------------------------------+
|                    AiProfilePlugin                          |
|  (Orchestrator: runs every 6h via Metro DI)                 |
+---------------------------+---------------------------------+
                            |
              +-------------+-------------+
              v                           v
     +------------------+       +-------------------+
     | Data Aggregator  |       | Outlier Detector   |
     | (BG/bolus/carbs)|       | (spike grouping)   |
     +--------+---------+       +-------------------+
              |                           |
              v                           v
     PersistenceLayer                AI Server
     (DB: BG, bolus, carbs)          (HTTP POST)
                                     |
                              +------v------+
                              | AiApiClient  |
                              | (Ktor/OkHttp)|
                              +--------------+
`

---

## Component Summary

| File | Purpose |
|------|---------|
| AiProfileConfig.kt | Configuration: aggressiveness, API URL/key, spike thresholds, analysis period. Serializable for Preferences storage |
| AiApiClient.kt | Interface contract — defines sendAnalysisRequest() and testConnection(). Enables swapping implementations (mocks for tests) |
| AiHttpClient.kt | Interface implementation via Ktor + OkHttp. Builds AI prompt, sends POST request with aggregated data, parses JSON response |
| AiConfigStore.kt | Read/write config to Android Preferences. Caches config in memory. Masks API key when displaying |
| DataAggregator.kt | Collects data from PersistenceLayer (BG readings, bolus, carbs). Calculates metrics: average glucose, CV%, TIR/TAR/TBR, 24h trend |
| OutlierDetector.kt | Groups spikes by day. Compares count of unexplained outliers (no bolus/carbs) against threshold from config. Classifies: NORMAL / MODERATE_ANOMALIES / HEAVY_ANOMALIES |
| SpikeInfo.kt | Defines BG spike as "spike" if diff >30 mg/dL (configurable). Searches for bolus/carbs in +-90min window per spike |
| AnalysisResult.kt | Aggregation result of all metrics. toJsonPayload() method builds JSON for AI server. isSufficientData() checks data quality |
| ProfileRecommendation.kt | Recommendation model: ISF shift %, basal shift %, confidence, reason. Methods isValid() (validation against aggressiveness) and simulateBgEffect() (BG prediction) |
| AiProfileBindings.kt | Metro DI binding — registers all module components in container. PluginBase gets ID=42 |

---

## Execution Flow

`
1. onStart() starts when AAPS boots
   |-- every 6 hours (CHECK_INTERVAL_MS) calls checkAndAnalyze()

2. checkAndAnalyze()
   |-- Checks: has 6h passed since last check
   |-- Checks: did we already recommend today (allowSameDayCorrection)
   -- calls analyzeAndRecommend(config)

3. analyzeAndRecommend(config) — full analysis cycle:
   |
   +-- [1] DataAggregator.collectAnalysisData()
   |    --> Collects BG/bolus/carbs for config.analysisDays days (7 or 30)
   |
   +-- [2] data.isSufficientData(config)
   |    --> Checks: CGM coverage >80%, days >= minDataDays, CV% <50%
   |         If no -> Skipped("Insufficient data")
   |
   +-- [3] OutlierDetector.detectAnomalies()
   |    --> Groups spikes by day, counts unexplained (no bolus/carbs)
   |         If days with anomalies >= 3 -> HEAVY_ANOMALIES -> Skipped
   |
   +-- [4] AiApiClient.sendAnalysisRequest(config, data)
   |    --> Builds prompt: "You are AI profile analyzer... here is data..."
   |         Sends POST to user-provided API URL with JSON body
   |         Parses response: {isfShiftPercent, basalShiftPercent, confidence, reason}
   |
   +-- [5] ProfileRecommendation.isValid(rec, config)
   |    --> Checks: |ISF shift| <= maxIsfShiftPercent(aggressiveness)
   |                 AND: |basal shift| <= maxBasalShiftPercent
   |                 AND: confidence >= minConfidenceThreshold()
   |
   +-- [6] simulateProfileChange(data, recommendation)
   |    --> Applies shifts to virtual profile copy
   |         Calculates predicted BG = avgGlucose + deltaBG
   |         If predicted <50 or >300 -> predictionBad=true
   |
   -- [7] Returns AnalysisOutcome.Success(recommendation)

4. showNotification(recommendation, config)
   |-- Builds push notification with "Reject" / "Apply" buttons
   |-- On "Apply" click — recommends applying to profile (via UI)
   -- cachedRecommendation updated, next checkAndAnalyze() skips same day
`

---

## Aggressiveness Levels

| Level | Max ISF shift | Max Basal shift | Min confidence | Max spikes/day |
|-------|---------------|-----------------|----------------|----------------|
| CALM    | +/-5%           | +/-5%             | >=90%           | 1              |
| MODERATE| +/-15%          | +/-10%            | >=75%           | 2              |
| AGGRESSIVE| +/-25%        | +/-20%            | >=60%           | 4              |

---

## Safety Guards (False Positive Prevention)

1. **Minimum data**: analysisDays >= minDataDays (default 3 days)
2. **CGM coverage**: cgmCoveragePercent >= config.minCoveragePercent (80%)
3. **Stability check**: CV% <50% — otherwise data considered unstable
4. **Spike filter**: BG spike without bolus/carbs in +-90min window -> unexplained. If count > maxSpikesPerDay per day -> skip analysis
5. **Once per day**: allowSameDayCorrection=false means we recommend at most once every 24h (default)
6. **Machine simulation**: AI recommendation passes through formula: deltaBG = -basalChangeUnits * ISF. If predicted BG <70 or >300 -> rejected
7. **Granularity**: Max 1 spike/day for CALM mode — protects from single CGM noise spikes

---

## Key Design Decisions

- **External API**: No heavy ML models on device. User enters their own endpoint (OpenAI, Anthropic, custom server).
- **Ktor + OkHttp**: Uses same pattern as NsKtorClient in core/nssdk — KMP compatible, shared HTTP stack.
- **Metro DI**: All bindings via @ContributesTo(AppScope::class) + @BindingContainer. Plugin gets ID=42 (unique).
- **Immutable workaround**: Kotlin data class is immutable -> for reducing confidence on failed simulation we create new instance via copy().
- **Strict prompt**: AI receives structured JSON metrics + instruction in format "you are AI analyzer... return only JSON".

---

## Build Dependencies

In implementation/build.gradle.kts already available:
- ktor-client-okhttp (androidMain and jvmMain)
- ktor-client-content-negotiation (commonMain)
- ktor-serialization-kotlinx-json (commonMain)
- kotlinx-serialization-json (via kotlinx-serialization plugin)

Module adds no new dependencies — uses existing ones.
