package com.sample.core.basic2.event


sealed interface DefaultUiEvent : AppUIEvent {
    data class Toast(val message: String) : AppUIEvent.Critical
    data class Snackbar(val message: String) : AppUIEvent.Critical
    data class Navigate(val route: String) : AppUIEvent.Critical
    data object Dialog : AppUIEvent.Critical
    data object Back : AppUIEvent.Critical
    data object ScrollToTop : AppUIEvent.NonCritical
    data class Log(val text: String) : AppUIEvent.NonCritical
}