package com.sample.feature.logger.basic.event

sealed interface GlobalUiEvent {
    data class Toast(val message: String) : GlobalUiEvent
    data class Snackbar(val message: String) : GlobalUiEvent
    data class Navigate(val route: String) : GlobalUiEvent
    object Dialog : GlobalUiEvent
    object Back : GlobalUiEvent
}