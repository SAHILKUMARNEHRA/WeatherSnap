package com.trackzio.weathersnap.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackzio.weathersnap.data.repo.ReportsRepository
import com.trackzio.weathersnap.domain.Report
import com.trackzio.weathersnap.ui.screens.draft.ReportDraftState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateReportUiState(
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saved: Boolean = false,
)

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val reportsRepository: ReportsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState

    fun consumeSaved() {
        _uiState.update { it.copy(saved = false) }
    }

    fun save(draft: ReportDraftState) {
        val weather = draft.weather ?: run {
            _uiState.update { it.copy(errorMessage = "No weather selected") }
            return
        }
        val photo = draft.photo ?: run {
            _uiState.update { it.copy(errorMessage = "Please capture a photo") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val report = Report(
                    id = 0,
                    createdAtEpochMs = System.currentTimeMillis(),
                    cityName = weather.cityName,
                    condition = weather.condition,
                    temperatureC = weather.temperatureC,
                    humidityPercent = weather.humidityPercent,
                    windSpeedMs = weather.windSpeedMs,
                    pressureHpa = weather.pressureHpa,
                    notes = draft.notes.trim(),
                    originalImagePath = photo.originalPath,
                    compressedImagePath = photo.compressedPath,
                    originalSizeBytes = photo.originalSizeBytes,
                    compressedSizeBytes = photo.compressedSizeBytes,
                )
                reportsRepository.insertReport(report)
                _uiState.update { it.copy(isSaving = false, saved = true) }
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = t.message ?: "Failed to save report",
                    )
                }
            }
        }
    }
}

