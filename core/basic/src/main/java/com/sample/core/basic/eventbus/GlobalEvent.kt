package com.sample.core.basic.eventbus

sealed class GlobalEvent {
    data class Toast(val message: String) : GlobalEvent()
    data class Snackbar(val message: String) : GlobalEvent()
    data class Navigate(val route: String) : GlobalEvent()
}