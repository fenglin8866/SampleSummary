package com.sample.feature.login

import androidx.lifecycle.viewModelScope
import com.sample.core.basic.ui.BaseViewModel
import com.sample.core.basic.ui.event.UIEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<LoginUIState, LoginUIIntent>(LoginUIState()) {

    override fun handleIntent(intent: LoginUIIntent) {
        when (intent) {
            is LoginUIIntent.OnLoginClicked -> onLoginClicked()
            is LoginUIIntent.OnNavigateToHome -> onNavigateToHome()
        }
    }


    private fun onLoginClicked() {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            delay(1000)
            updateState { copy(isLoading = false, isLoginSuccess = true) }
            sendEvent(UIEvent.Toast("登录成功"))
        }
    }

    private fun onNavigateToHome() {
        sendEvent(UIEvent.Navigate("home"))
    }
}