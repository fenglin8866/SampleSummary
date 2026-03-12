package com.sample.data.datastore.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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

    val darkMode = PreferenceFactory.boolean(
        dataStore,
        "dark_mode",
        false
    )

    val fontSize = PreferenceFactory.int(
        dataStore,
        "font_size",
        14
    )

    val language = PreferenceFactory.string(
        dataStore,
        "language",
        "en"
    )

    val uiSettingsFlow = combine(
        darkMode.flow,
        fontSize.flow,
        language.flow
    ) { dark, font, lang ->
        UiSettings(dark, font, lang)
    }.distinctUntilChanged()
}

data class UiSettings(
    val darkMode: Boolean,
    val fontSize: Int,
    val language: String
)