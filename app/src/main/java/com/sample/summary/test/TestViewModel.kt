package com.sample.summary.test

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestViewModel : BaseViewModel<MainUIState>(MainUIState()) {

    fun onLoginClicked() {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            delay(1000)
            updateState { copy(isLoading = false, username = "张三") }
            sendEvent(UIEvent.Toast("登录成功"))
        }
    }

    fun onNavigateToHome() {
        sendEvent(UIEvent.Navigate("home"))
    }
}
