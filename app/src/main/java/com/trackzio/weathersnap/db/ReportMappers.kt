package com.trackzio.weathersnap.db

import com.trackzio.weathersnap.domain.Report

fun ReportEntity.toDomain(): Report =
    Report(
        id = id,
        createdAtEpochMs = createdAtEpochMs,
        cityName = cityName,
        condition = condition,
        temperatureC = temperatureC,
        humidityPercent = humidityPercent,
        windSpeedMs = windSpeedMs,
        pressureHpa = pressureHpa,
        notes = notes,
        originalImagePath = originalImagePath,
        compressedImagePath = compressedImagePath,
        originalSizeBytes = originalSizeBytes,
        compressedSizeBytes = compressedSizeBytes,
    )

fun Report.toEntity(): ReportEntity =
    ReportEntity(
        id = id,
        createdAtEpochMs = createdAtEpochMs,
        cityName = cityName,
        condition = condition,
        temperatureC = temperatureC,
        humidityPercent = humidityPercent,
        windSpeedMs = windSpeedMs,
        pressureHpa = pressureHpa,
        notes = notes,
        originalImagePath = originalImagePath,
        compressedImagePath = compressedImagePath,
        originalSizeBytes = originalSizeBytes,
        compressedSizeBytes = compressedSizeBytes,
    )

