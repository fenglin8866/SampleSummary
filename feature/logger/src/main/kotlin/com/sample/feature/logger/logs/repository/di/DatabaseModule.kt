package com.sample.feature.logger.logs.repository.di

import android.content.Context
import com.sample.feature.logger.logs.repository.db.AppDatabase
import com.sample.feature.logger.logs.repository.db.LogDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase): LogDao {
        return database.logDao()
    }

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}