package com.sample.feature.set.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.feature.set.domain.DefaultBrowserGuideUseCase
import com.sample.feature.set.infra.DefaultBrowserChecker
import com.sample.feature.set.ui.BrowserUiEvent
import com.sample.feature.set.ui.BrowserUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrowserViewModel(
    private val guideUseCase: DefaultBrowserGuideUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<BrowserUiEvent>()
    val uiEvent: SharedFlow<BrowserUiEvent> = _uiEvent

    private val _uiState = MutableStateFlow(BrowserUiState())

    val uiState: StateFlow<BrowserUiState> = _uiState

    /** WebView 页面加载完成 */
    fun onPageLoaded(context: Context) {
        viewModelScope.launch {
            if (guideUseCase.shouldShowGuide(context)) {
                guideUseCase.markGuideShown()
                _uiEvent.emit(BrowserUiEvent.ShowDefaultBrowserGuide)
            }
        }
    }

    fun onGuideConfirmed() {
        viewModelScope.launch {
            _uiEvent.emit(BrowserUiEvent.LaunchRoleManager)
        }
    }

    fun onResume(context: Context) {
        _uiState.value = BrowserUiState(
            isDefaultBrowser =
                DefaultBrowserChecker.isDefaultBrowser(context)
        )
    }

    fun onRoleReturnFast() {
        viewModelScope.launch {
            _uiEvent.emit(BrowserUiEvent.OpenDefaultBrowserSettings)
        }
    }
}

