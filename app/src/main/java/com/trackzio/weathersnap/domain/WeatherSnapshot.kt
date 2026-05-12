package com.trackzio.weathersnap.domain

data class WeatherSnapshot(
    val cityName: String,
    val temperatureC: Double,
    val condition: String,
    val humidityPercent: Int,
    val windSpeedMs: Double,
    val pressureHpa: Int,
)

