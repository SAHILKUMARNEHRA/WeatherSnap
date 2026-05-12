package com.trackzio.weathersnap.domain

data class Report(
    val id: Long,
    val createdAtEpochMs: Long,
    val cityName: String,
    val condition: String,
    val temperatureC: Double,
    val humidityPercent: Int,
    val windSpeedMs: Double,
    val pressureHpa: Int,
    val notes: String,
    val originalImagePath: String,
    val compressedImagePath: String,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long,
)

