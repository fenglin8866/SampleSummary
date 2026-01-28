package com.sample.core.basic.event2

sealed interface GlobalUiEvent {
    data class Toast(val message: String) : GlobalUiEvent
    data class Snackbar(val message: String) : GlobalUiEvent
    data class Navigate(val route: String) : GlobalUiEvent
    data object Dialog : GlobalUiEvent
    data object Back : GlobalUiEvent
}