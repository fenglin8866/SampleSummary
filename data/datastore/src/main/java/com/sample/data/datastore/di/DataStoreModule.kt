package com.sample.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.sample.data.datastore.data.dataStore
import com.sample.data.datastore.proto.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext content: Context): DataStore<UserPreferences> {
        return content.dataStore
    }

}