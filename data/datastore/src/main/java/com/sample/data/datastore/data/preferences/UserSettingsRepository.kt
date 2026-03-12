package com.sample.data.datastore.data.preferences

import javax.inject.Inject


class UserSettingsRepository @Inject constructor(val settings: SettingsStore) {

    val uiSettings = settings.uiSettingsFlow

    suspend fun setDarkMode(enable: Boolean) {
        settings.darkMode.set(enable)
    }

    suspend fun setLanguage(lang: String) {
        settings.language.set(lang)
    }

    suspend fun setFontSize(size: Int) {
        settings.fontSize.set(size)
    }
}