package com.sample.feature.logger.logs.repository.di

import com.sample.feature.logger.logs.repository.LogRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ObjectDependencies {
    fun getRepository(): LogRepository
}