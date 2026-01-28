package com.sample.core.basic.event

sealed class MainUIEvent : AppUIEvent {

    data class ShowToast(val msg: String) : AppUIEvent.Critical
    object NavigateLogin : AppUIEvent.Critical

    data object ScrollToTop : AppUIEvent.NonCritical
    data class Log(val text: String) : AppUIEvent.NonCritical
}
