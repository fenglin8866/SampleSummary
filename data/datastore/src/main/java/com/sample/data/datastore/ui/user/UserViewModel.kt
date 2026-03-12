package com.sample.data.datastore.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.data.datastore.data.preferences.UiSettings
import com.sample.data.datastore.data.preferences.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repo: UserSettingsRepository) : ViewModel() {

    val uiSettings =
        repo.uiSettings
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                UiSettings(false, 14, "en")
            )


    fun saveLang(lang: String) {
        viewModelScope.launch {
            repo.setLanguage(lang)
        }
    }

    fun saveFontSize(size: String) {
        viewModelScope.launch {
            repo.setFontSize(size.toInt())
        }
    }
}