package com.sample.feature.logger.logs.repository.di

import com.sample.feature.logger.logs.repository.data.LoggerDataSource
import com.sample.feature.logger.logs.repository.data.LoggerLocalDataSource
import com.sample.feature.logger.logs.repository.db.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDataSource(dao: LogDao): LoggerDataSource {
        return LoggerLocalDataSource(dao)
    }

}