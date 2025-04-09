package com.sample.feature.login

sealed class LoginUIIntent {
    object OnLoginClicked : LoginUIIntent()
    object OnNavigateToHome : LoginUIIntent()
}
