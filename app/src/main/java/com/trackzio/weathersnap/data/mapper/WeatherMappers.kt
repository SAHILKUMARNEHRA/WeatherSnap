package com.trackzio.weathersnap.data.mapper

import com.trackzio.weathersnap.data.dto.CurrentWeatherDto
import com.trackzio.weathersnap.data.dto.GeocodingResultDto
import com.trackzio.weathersnap.domain.CitySuggestion
import com.trackzio.weathersnap.domain.WeatherSnapshot
import kotlin.math.roundToInt

fun GeocodingResultDto.toDomain(): CitySuggestion =
    CitySuggestion(
        name = name,
        country = country,
        region = admin1,
        latitude = latitude,
        longitude = longitude,
    )

fun CurrentWeatherDto.toDomain(cityName: String): WeatherSnapshot =
    WeatherSnapshot(
        cityName = cityName,
        temperatureC = temperatureC ?: 0.0,
        condition = weatherCodeToCondition(weatherCode),
        humidityPercent = humidityPercent ?: 0,
        windSpeedMs = windSpeedMs ?: 0.0,
        pressureHpa = (pressureHpa ?: 0.0).roundToInt(),
    )

private fun weatherCodeToCondition(code: Int?): String {
    return when (code) {
        0 -> "Clear"
        1, 2 -> "Mostly clear"
        3 -> "Overcast"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Freezing drizzle"
        61, 63, 65 -> "Rain"
        66, 67 -> "Freezing rain"
        71, 73, 75 -> "Snow"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm hail"
        else -> "Unknown"
    }
}

