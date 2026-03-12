package com.sample.lib.datastore.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.sample.lib.datastore.core.PreferenceKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * settingsStore.darkMode.flow
 * settingsStore.darkMode.set(true)
 *
 *class SettingsRepository @Inject constructor(
 *     private val settingsStore: SettingsStore
 * ) {
 *
 *     val darkModeFlow =
 *         settingsStore.darkMode.flow
 *
 *     suspend fun setDarkMode(value: Boolean) {
 *         settingsStore.darkMode.set(value)
 *     }
 * }
 *
 * @HiltViewModel
 * class SettingsViewModel @Inject constructor(
 *     repository: SettingsRepository
 * ) : ViewModel() {
 *
 *     val darkMode =
 *         repository.darkModeFlow
 *             .stateIn(
 *                 viewModelScope,
 *                 SharingStarted.WhileSubscribed(),
 *                 false
 *             )
 * }
 *
 */
@Singleton
class SettingsStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("settings")
            }
        )

    val darkMode =
        PreferenceKeys.boolean(
            dataStore,
            "dark_mode",
            false
        )

    val fontSize =
        PreferenceKeys.int(
            dataStore,
            "font_size",
            14
        )

    val language =
        PreferenceKeys.string(
            dataStore,
            "language",
            "en"
        )
}