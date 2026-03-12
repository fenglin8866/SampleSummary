package com.sample.lib.datastore.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceDelegate<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val default: T
) {

    val flow: Flow<T> =
        dataStore.data
            .map { it[key] ?: default }
            .distinctUntilChanged()

    suspend fun get(): T {
        return flow.first()
    }

    suspend fun set(value: T) {
        dataStore.edit {
            it[key] = value
        }
    }
}