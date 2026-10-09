# GM AAPS — Build Guide

## Сборка приложения

### Требования
- Android Studio (JDK 17+)
- `ANDROID_HOME` установлен на `C:\Users\Gleb\AppData\Local\Android\Sdk`
- Gradle 9.8+

### Команда сборки

```powershell
cd E:\AAPS\aaps4
$env:ANDROID_HOME="C:\Users\Gleb\AppData\Local\Android\Sdk"
.\gradlew assembleDebug --console=plain
```

### Сборка всех APK (debug)

Команда выше собирает все 5 вкусов приложения автоматически. Clean + rebuild:

```powershell
cd E:\AAPS\aaps4
$env:ANDROID_HOME="C:\Users\Gleb\AppData\Local\Android\Sdk"
.\gradlew clean assembleDebug --console=plain
```

## Расположение APK

| APK | Путь | Размер | Package Name |
|-----|------|--------|--------------|
| **Full (основной)** | `app/build/outputs/apk/full/debug/app-full-debug.apk` | ~214 MB | `app.gmaaps` |
| **PumpControl** | `app/build/outputs/apk/pumpcontrol/debug/app-pumpcontrol-debug.apk` | ~214 MB | `app.gmaaps.pumpcontrol` |
| **AAPS Client 1** | `app/build/outputs/apk/aapsclient/debug/app-aapsclient-debug.apk` | ~190 MB | `app.gmaaps.client` |
| **AAPS Client 2** | `app/build/outputs/apk/aapsclient2/debug/app-aapsclient2-debug.apk` | ~190 MB | `app.gmaaps.client2` |
| **AAPS Client 3** | `app/build/outputs/apk/aapsclient3/debug/app-aapsclient3-debug.apk` | ~190 MB | `app.gmaaps.client3` |

## Установка через ADB

```powershell
# Проверка подключения устройства
adb devices

# Установка основного APK (с заменой существующего)
adb install -r "E:\AAPS\aaps4\app\build\outputs\apk\full\debug\app-full-debug.apk"

# Если нужно установить рядом с оригинальным AAPS — используем разные package name:
# GM AAPS уже имеет package app.gmaaps, оригинальный AAPS использует app.aaps.
# Можно установить оба параллельно.

# Для клиентских APK (для телефонов)
adb install -r "E:\AAPS\aaps4\app\build\outputs\apk\aapsclient2\debug\app-aapsclient2-debug.apk"

# Установка на конкретное устройство (если подключено несколько)
adb -s <serial> install -r "путь\к\apk"

# Удаление приложения
adb uninstall app.gmaaps
adb uninstall app.gmaaps.pumpcontrol
```

## Версии

- **Version Name**: 4.0.0-dev-d
- **Version Code**: 1500
- **Compile SDK**: 37
- **Min SDK**: 26 (Android 8.0)

## Troubleshooting сборки

### Ошибка "No value passed for parameter"
Убедитесь, что в `MetroGraphs.kt` передаются все графы DI:
```kotlin
.create(applicationScope, context, memberInjector, databaseConfig, externalOptionsOverride, CoreObjectsGraph, AiProfileBindings)
```

### Ошибка Ktor dependencies
Проверьте `implementation/build.gradle.kts`:
- ktor-client-core
- ktor-client-okhttp
- ktor-content-negotiation
- ktor-serialization-json
