# WeatherSnap

Android app built with Kotlin + Jetpack Compose following the provided assignment PDF and UI flow video.

Created by Sahil Kumar.

## Requirements

- Android Studio (latest stable)
- JDK 17 (Android Studio bundled JDK works)

## Run

- Open this folder in Android Studio
- Sync Gradle
- Run the `app` configuration on an emulator/device

## Deliverables Checklist

- Kotlin + Jetpack Compose UI
- MVVM with ViewModel + StateFlow
- Coroutines for async work
- Hilt for DI
- Navigation Compose
- Retrofit + Gson converter + OkHttp logging interceptor
- Room database for saved reports (weather + notes + image paths + sizes + timestamp)
- Custom in-app camera using CameraX
- Captured image is compressed before saving report (shows original/compressed sizes)
- Saved Reports screen lists saved reports from Room and displays photo + weather + notes + sizes + timestamp

## Notes

- Uses Open‑Meteo (no API key)
- City suggestion caching is in-memory (per query)
- Reports are saved locally in Room DB
- Captured photos are stored in app internal storage under `files/photos`
- Camera uses CameraX (custom in-app camera screen)
