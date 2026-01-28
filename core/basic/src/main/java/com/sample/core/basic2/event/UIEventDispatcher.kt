package com.sample.core.basic2.event

import kotlinx.coroutines.flow.SharedFlow

interface UIEventDispatcher<E : UIEvent> {
    val events: SharedFlow<E>
    fun dispatch(event: E)
}
