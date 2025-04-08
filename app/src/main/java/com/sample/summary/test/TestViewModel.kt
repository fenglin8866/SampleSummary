package com.sample.summary.test

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestViewModel : BaseViewModel<MainUIState, UIIntent>(MainUIState()) {

    override fun onLoginClicked() {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            delay(1000)
            updateState { copy(isLoading = false, username = "张三") }
            sendEvent(UIEvent.Toast("登录成功"))
        }
    }

    override fun onNavigateToHome() {
        sendEvent(UIEvent.Navigate("home"))
    }
}