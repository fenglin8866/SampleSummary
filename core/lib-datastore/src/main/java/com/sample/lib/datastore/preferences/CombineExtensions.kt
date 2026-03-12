package com.sample.lib.datastore.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

data class UiSettings(
    val darkMode: Boolean,
    val fontSize: Int,
    val language: String
)

val SettingsStore.uiSettingsFlow: Flow<UiSettings>
    get() = combine(
        darkMode.flow,
        fontSize.flow,
        language.flow
    ) { dark, font, lang ->
        UiSettings(dark, font, lang)
    }.distinctUntilChanged()