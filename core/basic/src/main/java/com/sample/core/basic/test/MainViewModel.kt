package com.sample.core.basic.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.core.basic.ui.middleware.LoggingMiddleware
import com.sample.core.basic.ui.middleware.PermissionMiddleware
import com.sample.core.basic.ui.middleware.ThrottleMiddleware
import com.sample.core.basic.ui.event.UIEvent
import com.sample.core.basic.ui.dispatcher.IntentDispatcher
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val dispatcher = IntentDispatcher<MainUIIntent>(
        middlewares = listOf(
            LoggingMiddleware(),
            ThrottleMiddleware(intervalMs = 500),
            PermissionMiddleware(
                check = { intent -> checkIntentPermission(intent) },
                onDenied = { sendEvent(UIEvent.Toast("权限不足")) }
            )
        ),
        onIntentHandled = { intent -> handleIntent(intent) }
    )

    private fun sendEvent(toast: Any) {

    }

    fun dispatch(intent: MainUIIntent) {
        viewModelScope.launch {
            dispatcher.dispatch(intent)
        }
    }


    private fun checkIntentPermission(intent: MainUIIntent): Boolean {
        // 简化逻辑示意
        return when (intent) {
            is MainUIIntent.SubmitOrder -> false
            else -> true
        }
    }


    private fun handleIntent(intent: MainUIIntent) {
        // ...
    }
}