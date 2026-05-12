# WeatherSnap

Android app built with Kotlin + Jetpack Compose following the provided assignment PDF and UI flow video.

## Requirements

- Android Studio (latest stable)
- JDK 17 (Android Studio bundled JDK works)

## Run

- Open this folder in Android Studio
- Sync Gradle
- Run the `app` configuration on an emulator/device

## Notes

- Uses Open‑Meteo (no API key)
- City suggestion caching is in-memory (per query)
- Reports are saved locally in Room DB
- Camera uses CameraX (custom in-app camera screen)

