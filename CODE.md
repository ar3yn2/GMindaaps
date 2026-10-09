# GM AAPS — Code Documentation

## Структура проекта

```
E:\AAPS\aaps4\
├── app/                          # Основной модуль Android
│   └── src\main\kotlin\app\gmaaps\  # Package: app.gmaaps (вместо app.aaps)
│       ├── di\metro\             # Dependency Injection
│       │   ├── AppRootGraph.kt   # Корневой граф DI
│       │   ├── MetroGraphs.kt    # Фабрика графов
│       │   └── AiProfileBindings.kt  # Bindings для AI модуля
├── implementation/               # Базовый модуль логики (common)
│   └── src\commonMain\kotlin\app\aaps\profile\ai\
└── core\keys\                    # Enum NonKey для настроек
```

## Package Rename: app.aaps → app.gmaaps

Все файлы перемещены и переименованы. Package name изменён везде:
- `app/build.gradle.kts` — applicationId = "app.gmaaps" (и flavour-версии)
- Директория `app/src/main/kotlin/app/aaps/` → `app/gmaaps/`
- Все import в 5000+ файлах обновлены

## AI Profile Analyzer Module

Пакет: `app.aaps.profile.ai` (внутри `:implementation/commonMain`)

### Архитектура модуля

```
AI Profile Analyzer:
├── AiProfilePlugin.kt      # Плагин-обёртка (PluginBase) — запускается каждые 6 часов
├── DataAggregator.kt       # Сбор данных из PersistenceLayer: BG, boluses, carbs
├── OutlierDetector.kt      # Обнаружение необъяснимых выбросов глюкозы
├── SpikeInfo.kt            # Модель spike + детекция скачков BG
├── ProfileRecommendation.kt# Результат рекомендации (ISF/Basal shift)
├── AnalysisResult.kt       # Агрегированные статистические данные
├── AiApiClient.kt          # HTTP клиент для AI API (ktor okhttp)
├── AiHttpClient.kt         # HTTP клиент (jvmSharedMain, Ktor)
├── AiProfileConfig.kt      # Конфигурация: агрессивность, API URL, ключи
└── AiConfigStore.kt        # Сохранение конфига в SharedPreferences через NonKey
```

### Ключевые файлы AI модуля

#### AiProfilePlugin.kt
- Наследуется от `PluginBase` — регистрируется как плагин AAPS
- Запускает проверку каждые 6 часов (`CHECK_INTERVAL_MS = 6h`)
- При `onStart()` запускает coroutine с циклом анализа
- При `onStop()` останавливает выполнение
- Использует DataAggregator + OutlierDetector для сбора и проверки данных

#### DataAggregator.kt
- Читает данные из `PersistenceLayer`:
  - BG readings (фильтрация валидных >20 и <600 mg/dL)
  - Boluses (amount > 0)
  - Carbs (amount > 0)
- За период analysisDays (7 или 30 дней по умолчанию)
- Рассчитывает: средний BG, медиана, CV%, TIR/TAR/TBR, bolus/carbs на сутки

#### OutlierDetector.kt
- Находит spike'и через `SpikeInfo.detectFromReadings()`
- Группирует spike'и по дням
- Определяет тип аномалии: NORMAL / MODERATE_ANOMALIES / HEAVY_ANOMALIES
- Проверяет покрытие CGM (cgmCoverage)

#### SpikeInfo.kt
- Детекция скачков BG выше threshold (по умолчанию 30 mg/dL)
- Для каждого spike ищет bolus/carbs в окне [-90min .. +30min]
- `isExplained()` = true если найден соответствующий bolus или carbs

#### AiProfileConfig.kt
```kotlin
data class AiProfileConfig(
    val aggressiveness: AiAggressiveness = MODERATE, // CALM / MODERATE / AGGRESSIVE
    val apiUrl: String = "https://api.yourai.com/v1/analyze",
    val apiKey: String = "",
    val analysisDays: Int = 7,                       // 7 или 30 дней
    val spikeThreshold: Int = 30,                    // mg/dL для детекции скачка
    val maxSpikesPerDay: Int = 2,                    // порог необъяснимых spike'ов
    val allowSameDayCorrection: Boolean = false      // предлагать ли изменения в тот же день
)

enum class AiAggressiveness {
    CALM ->     maxIsfShift=5%,  maxBasalShift=5%,  minConfidence=90%
    MODERATE -> maxIsFShift=15%, maxBasalShift=10%, minConfidence=75%
    AGGRESSIVE->maxIsFShift=25%, maxBasalShift=20%, minConfidence=60%
}
```

#### AiConfigStore.kt
- Сохраняет конфиг в SharedPreferences
- Использует NonKey enums для ключей:
  - `BooleanNonKey.AiProfileEnabled` — включён ли плагин
  - `IntNonKey.AiAnalysisDays` — период анализа (7/30)
  - `StringNonKey.AiApiUrl` — URL AI API
  - `IntNonKey.AiAggressiveness` — уровень агрессивности

#### AiApiClient.kt + AiHttpClient.kt
- AiApiClient: обёртка над HTTP клиентом, принимает конфиг и агрегированные данные
- AiHttpClient (jvmSharedMain): Ktor client с OkHttp engine
- Fallback: если API ключ пустой — используется локальная эвристика

### DI Integration

#### AppRootGraph.kt (строки ~545-555)
```kotlin
@Includes coreObjects: CoreObjectsGraph,
@Includes aiProfileBindings: AiProfileBindings
): AppRootGraph
```

#### MetroGraphs.kt
```kotlin
import app.gmaaps.di.metro.AiProfileBindings
// ...
.create(applicationScope, context, memberInjector, databaseConfig, 
        externalOptionsOverride, CoreObjectsGraph, AiProfileBindings)
```

#### AiProfileBindings.kt
- Содержит `@InstallIn` аннотации для связывания AI классов через Metro DI

## NonKey Enums (AI Settings)

### BooleanNonKey.kt
Добавлен ключ: `AiProfileEnabled` — глобальный тумблер AI Profile Analyzer

### IntNonKey.kt
Добавлены ключи: `AiAnalysisDays` (7/30), `AiAggressiveness` (0=Calm, 1=Moderate, 2=Aggressive)

### StringNonKey.kt
Добавлен ключ: `AiApiUrl` — пользовательский URL AI API

## Сборка и зависимости

### Gradle modules
- `:implementation` содержит commonMain с AI модулем
- `:app` зависит от `:implementation` и содержит Android-specific код

### Ktor dependencies (в implementation/build.gradle.kts)
```kotlin
ktor-client-core
ktor-client-okhttp
ktor-content-negotiation
ktor-serialization-json
```

## Troubleshooting DI ошибок

**Ошибка**: "No value passed for parameter 'aiProfileBindings'"
- **Причина**: В `MetroGraphs.kt` не передан параметр в `.create()`
- **Решение**: Добавить `AiProfileBindings` как второй аргумент в `.create(..., CoreObjectsGraph)`

**Ошибка при toggle AI плагина**: приложение вылетает
- **Причина**: Отсутствует `@Includes aiProfileBindings: AiProfileBindings` в Factory.create() AppRootGraph
- **Решение**: Добавить в AppRootGraph.kt и импортировать `app.gmaaps.di.metro.AiProfileBindings`
