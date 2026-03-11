package com.sample.data.datastore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.Serializable

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore: DataStore<UserPreferences> by dataStore(
    fileName = USER_PREFERENCES_NAME,
    serializer = PreferencesSerializer,
)

@Serializable
data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)

/**
 * 将两个按钮合并为一个状态。不要使用两个布尔值来表示排序顺序，而是使用一个枚举值。
 * 减少状态空间
 */
enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}