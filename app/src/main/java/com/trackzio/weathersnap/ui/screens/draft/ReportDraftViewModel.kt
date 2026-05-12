package com.trackzio.weathersnap.ui.screens.draft

import androidx.lifecycle.ViewModel
import com.trackzio.weathersnap.domain.WeatherSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DraftPhoto(
    val originalPath: String,
    val compressedPath: String,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long,
)

data class ReportDraftState(
    val weather: WeatherSnapshot? = null,
    val photo: DraftPhoto? = null,
    val notes: String = "",
)

@HiltViewModel
class ReportDraftViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ReportDraftState())
    val state: StateFlow<ReportDraftState> = _state

    fun setWeather(weather: WeatherSnapshot) {
        _state.update { it.copy(weather = weather) }
    }

    fun setNotes(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    fun setPhoto(photo: DraftPhoto) {
        _state.update { it.copy(photo = photo) }
    }

    fun clear() {
        _state.value = ReportDraftState()
    }
}

