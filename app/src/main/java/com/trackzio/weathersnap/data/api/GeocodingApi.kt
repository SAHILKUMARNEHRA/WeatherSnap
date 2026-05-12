package com.trackzio.weathersnap.data.api

import com.trackzio.weathersnap.data.dto.GeocodingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json",
    ): GeocodingResponseDto
}

