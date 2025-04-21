package com.sample.core.basic.eventbus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object GlobalEventBus {
    private const val KEY = "global_event"
    private val _events = MutableSharedFlow<GlobalEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<GlobalEvent> = _events
    suspend fun emit(event: GlobalEvent) {
        _events.emit(event)
    }
    fun launchToast(scope: CoroutineScope, message: String) {
        scope.launch { emit(GlobalEvent.Toast(message)) }
    }
    fun launchSnackbar(scope: CoroutineScope, message: String) {
        scope.launch { emit(GlobalEvent.Snackbar(message)) }
    }
    fun launchNavigate(scope: CoroutineScope, route: String) {
        scope.launch { emit(GlobalEvent.Navigate(route)) }
    }
    // 快捷静态方法（默认用 MainScope）
    private val mainScope = CoroutineScope(Dispatchers.Main)
    fun toast(message: String) = launchToast(mainScope, message)
    fun snackbar(message: String) = launchSnackbar(mainScope, message)
    fun navigate(route: String) = launchNavigate(mainScope, route)
}