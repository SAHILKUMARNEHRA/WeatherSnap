package com.trackzio.weathersnap.data.repo

import com.trackzio.weathersnap.data.api.GeocodingApi
import com.trackzio.weathersnap.data.api.WeatherApi
import com.trackzio.weathersnap.data.mapper.toDomain
import com.trackzio.weathersnap.domain.CitySuggestion
import com.trackzio.weathersnap.domain.WeatherSnapshot
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApi,
    private val weatherApi: WeatherApi,
) : WeatherRepository {

    private val cacheMutex = Mutex()
    private val cityCache = LinkedHashMap<String, List<CitySuggestion>>(50, 0.75f, true)

    override suspend fun searchCities(query: String): List<CitySuggestion> {
        val key = query.trim().lowercase()
        if (key.length < 3) return emptyList()

        cacheMutex.withLock {
            cityCache[key]?.let { return it }
        }

        val response = geocodingApi.searchCity(name = key)
        val mapped = response.results.orEmpty().map { it.toDomain() }

        cacheMutex.withLock {
            cityCache[key] = mapped
            if (cityCache.size > 50) {
                val firstKey = cityCache.entries.firstOrNull()?.key
                if (firstKey != null) cityCache.remove(firstKey)
            }
        }

        return mapped
    }

    override suspend fun getWeather(city: CitySuggestion): WeatherSnapshot {
        val response = weatherApi.getForecast(latitude = city.latitude, longitude = city.longitude)
        val current = response.current ?: error("Missing current weather")
        return current.toDomain(cityName = city.displayName)
    }
}

