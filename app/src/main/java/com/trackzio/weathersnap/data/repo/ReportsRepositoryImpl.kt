package com.trackzio.weathersnap.data.repo

import com.trackzio.weathersnap.db.ReportDao
import com.trackzio.weathersnap.db.toDomain
import com.trackzio.weathersnap.db.toEntity
import com.trackzio.weathersnap.domain.Report
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsRepositoryImpl @Inject constructor(
    private val dao: ReportDao,
) : ReportsRepository {

    override fun observeReports(): Flow<List<Report>> {
        return dao.observeAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertReport(report: Report): Long {
        return dao.insert(report.toEntity())
    }
}

