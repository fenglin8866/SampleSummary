package com.sample.feature.logger.logs.repository.di

import com.sample.feature.logger.logs.repository.data.LogDataSource
import com.sample.feature.logger.logs.repository.data.LogInMemoryDataSource
import com.sample.feature.logger.logs.repository.data.LogLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerDataSourceModule {
    @Binds
    @Singleton
    @Database
    abstract fun bindLocalDataSource(impl: LogLocalDataSource): LogDataSource

    @Binds
    @InMemory
    @Singleton
    abstract fun bindMemoryDataSource(impl: LogInMemoryDataSource): LogDataSource

}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Database

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemory