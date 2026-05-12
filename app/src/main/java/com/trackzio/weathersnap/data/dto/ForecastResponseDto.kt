package com.trackzio.weathersnap.data.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponseDto(
    @SerializedName("current")
    val current: CurrentWeatherDto?,
)

data class CurrentWeatherDto(
    @SerializedName("temperature_2m")
    val temperatureC: Double?,
    @SerializedName("relative_humidity_2m")
    val humidityPercent: Int?,
    @SerializedName("weather_code")
    val weatherCode: Int?,
    @SerializedName("wind_speed_10m")
    val windSpeedMs: Double?,
    @SerializedName("surface_pressure")
    val pressureHpa: Double?,
)

