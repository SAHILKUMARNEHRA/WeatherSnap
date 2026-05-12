package com.trackzio.weathersnap.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY createdAtEpochMs DESC")
    fun observeAll(): Flow<List<ReportEntity>>

    @Insert
    suspend fun insert(report: ReportEntity): Long
}

