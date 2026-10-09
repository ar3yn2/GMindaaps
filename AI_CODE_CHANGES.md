# AAPS AI Profile Analyzer — Changes Documentation

## Overview
AI Profile Analyzer is a plugin for **AAPS 4.0.0-dev-d** (version code 1500) that analyzes glucose profiles and recommends ISF/basal adjustments based on spike patterns over configurable periods (7 or 30 days).

---

## Module Structure

All AI-related code lives in the `:implementation` module under:
```
implementation/src/commonMain/kotlin/app/aaps/profile/ai/
```

### Files Created/Modified

| File | Status | Description |
|------|--------|-------------|
| `AiProfileConfig.kt` | **New** | Configuration enum + data class (aggressiveness, API URL, analysis days, spike settings) |
| `AiApiClient.kt` | **Modified** | Interface for AI communication (`generateRecommendation`, `sendAnalysisRequest`, `testConnection`) |
| `AiConfigStore.kt` | **Modified** | Reads/writes preferences to storage via `Preferences` interface |
| `DataAggregator.kt` | **Modified** | Collects BG, bolus, carbs data from `PersistenceLayer`; calculates avg/median glucose, TIR/TAR/TBR, spike detection, A1c estimate, trend analysis |
| `SpikeInfo.kt` | **New** | Spike event model with bolus/carbs correlation check (90min lookback window) |
| `OutlierDetector.kt` | **Modified** | Detects anomalous spikes (unexplained glucose rises), counts per-day anomalies, determines anomaly type |
| `ProfileRecommendation.kt` | **Modified** | Result model with ISF shift %, basal shift %, confidence; validation against aggressiveness limits; simulation helpers |
| `AnalysisResult.kt` | **Modified** | Aggregated analysis data (glucose stats, coverage, trends) |
| `AiHttpClient.kt` | **Modified** | Ktor-based HTTP client for AI API calls (moved to jvmSharedMain) |
| `AiProfilePlugin.kt` | **Modified** | Main plugin: lifecycle (`onStart`, `onStop`), scheduled analysis every 6 hours, simulation validation, notifications |

### Integration Files

| File | Status | Description |
|------|--------|-------------|
| `app/src/main/kotlin/app/aaps/di/metro/AiProfileBindings.kt` | **Modified** | Metro bindings: provides `AiConfigStore`, `AiHttpClient`, `AiProfilePlugin`; registers plugin at `@IntKey(42)` with `@IntoMap` |
| `core/keys/src/commonMain/kotlin/app/aaps/core/keys/IntNonKey.kt` | **Modified** | Added AI preference keys: AiAnalysisDays, AiMinCoveragePercent, AiSpikeThreshold, AiMaxSpikesPerDay, AiSpikeLookbackMinutes, AiMinDataDays, AiUpdateIntervalHours |
| `core/keys/src/commonMain/kotlin/app/aaps/core/keys/StringNonKey.kt` | **Modified** | Added AI keys: AiApiUrl, AiApiKey, AiAggressiveness |
| `core/keys/src/commonMain/kotlin/app/aaps/core/keys/BooleanNonKey.kt` | **Modified** | Added AI key: AiProfileEnabled, AiAllowSameDayCorrection |
| `core/interfaces/src/commonMain/kotlin/app/aaps/core/interfaces/notifications/NotificationId.kt` | **Modified** | Added `AI_PROFILE_RECOMMENDATION` and `AI_PROFILE_ERROR` notification IDs |

### Build Configuration

| File | Status | Description |
|------|--------|-------------|
| `implementation/build.gradle.kts` | **Modified** | Added dependencies for AI module (ktor-client-cio, ktor-serialization-kotlinx-json, kotlinx-coroutines-core) |
| `gradle/libs.versions.toml` | **Modified** | Added Ktor version catalog entries |

---

## Key Architectural Decisions

### 1. Aggressiveness Levels
Three presets control the AI sensitivity:

| Level | Max ISF Shift | Max Basal Shift | Min Confidence | Max Spikes/Day |
|-------|--------------|-----------------|----------------|----------------|
| CALM | ±5% | ±5% | 90% | 1 |
| MODERATE | ±15% | ±10% | 75% | 2 (default) |
| AGGRESSIVE | ±25% | ±20% | 60% | 4 |

### 2. Spike Protection Logic
- **90-minute lookback window** — each spike checked for corresponding bolus/carbs within ±90 min
- **MaxSpikesPerDay config parameter** (default: 2) — if spikes exceed this, AI skips recommendation (protects against missed carbs)
- **Same-day correction guard** (`allowSameDayCorrection`: false by default) — one recommendation per day max

### 3. Simulation Validation
Before recommending changes, the plugin simulates the effect:
```
estimatedTDD = bolusUnits + basalEstimate(bolusUnits * 0.8 or * 1.5)
predictedBgChange = -basalChangeUnits * avgGlucose / ISF_multiplier
predictedAvgGlucose = current_avg + predictedBgChange
```
Simulation fails (rejects recommendation) if: `predicted < 50`, `predicted > 300`, or change > ±100 mg/dL.

### 4. Data Sufficiency Check
AI only runs when:
- CGM coverage ≥ configured minimum (default: 80%)
- Analysis period meets minimum days threshold (default: 3)
- Enough BG readings to calculate meaningful statistics

---

## Bug Fixes During Development

1. **`AiProfileBindings.kt:32`** — `AiHttpClient(configStore) { msg -> ... }` called with wrong args; constructor accepts only default `HttpClient()`. Fixed by removing unused config/logger params.
2. **`PluginDescription` constructor** — used non-existent parameterized constructor `PluginDescription(id=42, name="...", tag="...")`; fixed to builder style: `PluginDescription().apply { mainType = PluginType.APS; pluginName = TextRef.Literal(...) }`.
3. **`@IntKey(42)` multibinding** — missing `@IntoMap` annotation causing Metro compile error. Added `@IntoMap` above the entry function.

---

## API Integration Points

| Component | Purpose |
|-----------|---------|
| `AiConfigStore.getConfig()` | Fetches AI config from preferences (async) |
| `AiApiClient.generateRecommendation(config, analysisResult, anomalies)` | Sends data to external AI API and parses response |
| `DataAggregator.collectAnalysisData(config)` | Pulls CGM/bolus/carbs from PersistenceLayer for last N days |
| `OutlierDetector.detectAnomalies(config)` | Classifies spikes as explained/unexplained |

External AI API endpoint is configurable via preferences (default: `https://api.yourai.com/v1/analyze`). Bearer token auth via stored API key.
