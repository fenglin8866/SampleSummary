package com.sample.summary.test

sealed class UIEvent {
    data class Toast(val message: String) : UIEvent()
    data class Snackbar(val message: String) : UIEvent()
    data class Navigate(val route: String) : UIEvent()
    object Back : UIEvent()
}