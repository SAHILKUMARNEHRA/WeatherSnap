package com.trackzio.weathersnap.di

import android.content.Context
import androidx.room.Room
import com.trackzio.weathersnap.db.AppDatabase
import com.trackzio.weathersnap.db.ReportDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "weathersnap.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideReportDao(db: AppDatabase): ReportDao = db.reportDao()
}

