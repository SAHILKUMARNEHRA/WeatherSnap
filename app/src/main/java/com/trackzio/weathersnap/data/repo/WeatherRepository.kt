package com.trackzio.weathersnap.data.repo

import com.trackzio.weathersnap.domain.CitySuggestion
import com.trackzio.weathersnap.domain.WeatherSnapshot

interface WeatherRepository {
    suspend fun searchCities(query: String): List<CitySuggestion>
    suspend fun getWeather(city: CitySuggestion): WeatherSnapshot
}

