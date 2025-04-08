package com.sample.summary.test

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<LoginUIState, UIIntent>(LoginUIState()) {

    override fun onLoginClicked() {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            delay(1000)
            updateState { copy(isLoading = false, isLoginSuccess = true) }
            sendEvent(UIEvent.Toast("登录成功"))
        }
    }

    override fun onNavigateToHome() {
        sendEvent(UIEvent.Navigate("home"))
    }
}