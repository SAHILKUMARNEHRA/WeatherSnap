package com.trackzio.weathersnap.domain

data class CitySuggestion(
    val name: String,
    val country: String?,
    val region: String?,
    val latitude: Double,
    val longitude: Double,
) {
    val displayName: String
        get() = listOfNotNull(name, country).joinToString(", ")

    val displayDetail: String
        get() = listOfNotNull(name, region, country).joinToString(", ")
}

