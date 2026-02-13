package com.sample.feature.set.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sample.core.basic2.viewmodel.BaseUIEventViewModel
import com.sample.feature.set.domain.DefaultBrowserGuideUseCase
import com.sample.feature.set.infra.DefaultBrowserChecker
import com.sample.feature.set.ui.contract.BrowserUiEvent
import com.sample.feature.set.ui.contract.BrowserUiState
import kotlinx.coroutines.launch

class BrowserViewModel(
    private val guideUseCase: DefaultBrowserGuideUseCase
) : BaseUIEventViewModel<BrowserUiState, BrowserUiEvent>(BrowserUiState()) {

    /** WebView 页面加载完成 */
    fun onPageLoaded(context: Context) {
        viewModelScope.launch {
            if (guideUseCase.shouldShowGuide(context)) {
                guideUseCase.markGuideShown()
                sendEvent(BrowserUiEvent.ShowDefaultBrowserGuide)
            }
        }
    }

    fun onDefaultDialog() {
        viewModelScope.launch {
            sendEvent(BrowserUiEvent.LaunchRoleManager)
        }
    }

    fun onResume(context: Context) {
        updateState (BrowserUiState(
            isDefaultBrowser =
                DefaultBrowserChecker.isDefaultBrowser(context)
        ))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BrowserViewModel(
                    guideUseCase = DefaultBrowserGuideUseCase(
                        checker = DefaultBrowserChecker
                    )
                ) as T
            }
        }
    }
}

