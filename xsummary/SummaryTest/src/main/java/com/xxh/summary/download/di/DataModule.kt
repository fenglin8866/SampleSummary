package com.xxh.summary.download.di

import android.content.Context
import com.sample.lib.network.core.NetworkManagerMultiBaseUrl
import com.xxh.summary.download.data.db.AppDatabase
import com.xxh.summary.download.data.db.DownloadDao
import com.xxh.summary.download.data.remote.api.AppApi
import com.xxh.summary.download.data.remote.config.HostManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 如果 Module 里全是 @Provides 方法（没有抽象方法/ @Binds），Kotlin 中应该用 object，避免 Hilt 额外实例化。
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppApi(): AppApi {
        return NetworkManagerMultiBaseUrl.create(HostManager.getAppBaseUrl(), AppApi::class.java)
    }

    @Provides
    fun provideDownloadDao(appDatabase: AppDatabase): DownloadDao {
        return appDatabase.downloadDao()
    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

}