package com.sample.lib.datastore.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceFactory {

    fun boolean(
        dataStore: DataStore<Preferences>,
        name: String,
        default: Boolean
    ) = PreferenceEntry(
        dataStore,
        booleanPreferencesKey(name),
        default
    )

    fun int(
        dataStore: DataStore<Preferences>,
        name: String,
        default: Int
    ) = PreferenceEntry(
        dataStore,
        intPreferencesKey(name),
        default
    )

    fun string(
        dataStore: DataStore<Preferences>,
        name: String,
        default: String
    ) = PreferenceEntry(
        dataStore,
        stringPreferencesKey(name),
        default
    )
}