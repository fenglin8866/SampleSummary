package com.sample.core.basic.event

import kotlinx.coroutines.flow.SharedFlow

interface UIEventDispatcher<E : UIEvent> {
    val events: SharedFlow<E>
    fun dispatch(event: E)
}
