package com.sample.summary.test

data class MainUIState(
    val isLoading: Boolean = false,
    val username: String = "",
    val error: String? = null
)