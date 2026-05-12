package com.trackzio.weathersnap.data.repo

import com.trackzio.weathersnap.domain.Report
import kotlinx.coroutines.flow.Flow

interface ReportsRepository {
    fun observeReports(): Flow<List<Report>>
    suspend fun insertReport(report: Report): Long
}

