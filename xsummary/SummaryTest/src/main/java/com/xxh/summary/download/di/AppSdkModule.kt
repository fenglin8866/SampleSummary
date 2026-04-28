package com.xxh.summary.download.di

import com.xxh.summary.download.data.sdk.AppCenterSdk
import com.xxh.summary.download.data.sdk.RealAppCenterSdk
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppSdkModule {
    @Singleton
    @Binds
    fun bindsCarRepository(repository: RealAppCenterSdk): AppCenterSdk
}