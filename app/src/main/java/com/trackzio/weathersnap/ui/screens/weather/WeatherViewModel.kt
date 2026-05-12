package com.trackzio.weathersnap.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackzio.weathersnap.data.repo.WeatherRepository
import com.trackzio.weathersnap.domain.CitySuggestion
import com.trackzio.weathersnap.domain.WeatherSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherUiState(
    val query: String = "",
    val helperText: String = "Enter more than 2 letters to start city suggestions.",
    val suggestions: List<CitySuggestion> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val isLoadingWeather: Boolean = false,
    val selectedCity: CitySuggestion? = null,
    val weather: WeatherSnapshot? = null,
    val errorMessage: String? = null,
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state

    private var suggestionsJob: Job? = null

    fun onQueryChange(value: String) {
        _state.update {
            it.copy(
                query = value,
                errorMessage = null,
                weather = null,
                selectedCity = null,
            )
        }
        loadSuggestionsDebounced(value)
    }

    fun onSelectSuggestion(city: CitySuggestion) {
        _state.update { it.copy(query = city.displayDetail, selectedCity = city, suggestions = emptyList()) }
        fetchWeatherForSelectedCity()
    }

    fun onSearchClick() {
        fetchWeatherForSelectedCity()
    }

    private fun loadSuggestionsDebounced(query: String) {
        suggestionsJob?.cancel()
        val normalized = query.trim()
        if (normalized.length < 3) {
            _state.update { it.copy(suggestions = emptyList(), isLoadingSuggestions = false) }
            return
        }

        suggestionsJob = viewModelScope.launch {
            _state.update { it.copy(isLoadingSuggestions = true) }
            delay(300)
            try {
                val cities = repository.searchCities(normalized)
                _state.update { it.copy(suggestions = cities, isLoadingSuggestions = false) }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        suggestions = emptyList(),
                        isLoadingSuggestions = false,
                        errorMessage = t.message ?: "Failed to load city suggestions",
                    )
                }
            }
        }
    }

    private fun fetchWeatherForSelectedCity() {
        val city = _state.value.selectedCity ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoadingWeather = true, errorMessage = null, weather = null) }
            try {
                val weather = repository.getWeather(city)
                _state.update { it.copy(isLoadingWeather = false, weather = weather) }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        isLoadingWeather = false,
                        weather = null,
                        errorMessage = t.message ?: "Failed to load weather",
                    )
                }
            }
        }
    }
}

