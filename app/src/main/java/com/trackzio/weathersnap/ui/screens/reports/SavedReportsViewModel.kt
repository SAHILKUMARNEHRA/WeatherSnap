package com.trackzio.weathersnap.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackzio.weathersnap.data.repo.ReportsRepository
import com.trackzio.weathersnap.domain.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    repository: ReportsRepository,
) : ViewModel() {
    val reports: StateFlow<List<Report>> = repository.observeReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

