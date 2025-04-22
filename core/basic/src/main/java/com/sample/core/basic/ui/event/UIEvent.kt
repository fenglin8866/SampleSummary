package com.sample.core.basic.ui.event

/**
 * UI事件
 */
sealed class UIEvent {
    data class Toast(val message: String) : UIEvent()
    data class Snackbar(val message: String) : UIEvent()
    data class Navigate(val route: String) : UIEvent()
    object Dialog : UIEvent()
    object Back : UIEvent()
}