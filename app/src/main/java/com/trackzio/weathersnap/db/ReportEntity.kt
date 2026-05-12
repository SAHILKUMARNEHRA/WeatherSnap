package com.trackzio.weathersnap.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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

