package com.trackzio.weathersnap.di

import com.trackzio.weathersnap.data.repo.ReportsRepository
import com.trackzio.weathersnap.data.repo.ReportsRepositoryImpl
import com.trackzio.weathersnap.data.repo.WeatherRepository
import com.trackzio.weathersnap.data.repo.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindReportsRepository(impl: ReportsRepositoryImpl): ReportsRepository
}
