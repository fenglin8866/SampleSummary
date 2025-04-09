package com.sample.core.basic.ui

sealed class UIEvent {
    data class Toast(val message: String) : UIEvent()
    data class Snackbar(val message: String) : UIEvent()
    data class Navigate(val route: String) : UIEvent()
    object Dialog : UIEvent()
    object Back : UIEvent()
}