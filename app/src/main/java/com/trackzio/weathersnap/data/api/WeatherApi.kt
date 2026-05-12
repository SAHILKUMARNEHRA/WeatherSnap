package com.trackzio.weathersnap.data.api

import com.trackzio.weathersnap.data.dto.ForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,surface_pressure",
        @Query("timezone") timezone: String = "auto",
    ): ForecastResponseDto
}

