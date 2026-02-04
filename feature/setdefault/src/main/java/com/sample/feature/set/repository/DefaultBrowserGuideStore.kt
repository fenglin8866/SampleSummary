package com.sample.feature.set.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class DefaultBrowserGuideStore(
    private val dataStore: DataStore<Preferences>
) {

    val lastGuideTimeFlow: Flow<Long> =
        dataStore.data.map { prefs ->
            prefs[DefaultBrowserDataStore.LAST_GUIDE_TIME] ?: 0L
        }

    suspend fun updateGuideTime() {
        dataStore.edit { prefs ->
            prefs[DefaultBrowserDataStore.LAST_GUIDE_TIME] =
                System.currentTimeMillis()
        }
    }

    fun cooldownMillis(): Long =
        TimeUnit.DAYS.toMillis(DefaultBrowserConfig.GUIDE_COOLDOWN_DAYS.toLong())
}