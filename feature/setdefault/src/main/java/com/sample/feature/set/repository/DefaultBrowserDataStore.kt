package com.sample.feature.set.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DefaultBrowserDataStore {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "default_browser"
    )

    val LAST_GUIDE_TIME = longPreferencesKey("last_guide_time")
}
