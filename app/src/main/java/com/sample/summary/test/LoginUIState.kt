package com.sample.summary.test

data class LoginUIState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val errorMessage: String? = null
)