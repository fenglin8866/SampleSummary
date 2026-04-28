package com.xxh.summary.download.di

import com.sample.lib.network.core.NetworkManagerMultiBaseUrl
import com.xxh.summary.download.data.remote.api.AppApi
import com.xxh.summary.download.data.remote.config.HostManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideAppApi(): AppApi {
        return NetworkManagerMultiBaseUrl.create(HostManager.getAppBaseUrl(), AppApi::class.java)
    }

}