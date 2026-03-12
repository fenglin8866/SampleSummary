package com.sample.lib.datastore.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {

    fun boolean(
        dataStore: DataStore<Preferences>,
        name: String,
        default: Boolean
    ) = PreferenceDelegate(
        dataStore,
        booleanPreferencesKey(name),
        default
    )

    fun int(
        dataStore: DataStore<Preferences>,
        name: String,
        default: Int
    ) = PreferenceDelegate(
        dataStore,
        intPreferencesKey(name),
        default
    )

    fun string(
        dataStore: DataStore<Preferences>,
        name: String,
        default: String
    ) = PreferenceDelegate(
        dataStore,
        stringPreferencesKey(name),
        default
    )
}