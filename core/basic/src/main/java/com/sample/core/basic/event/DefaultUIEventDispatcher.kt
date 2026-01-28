package com.sample.core.basic.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DefaultUIEventDispatcher<E : UIEvent>(
    private val scope: CoroutineScope,
    replay: Int = 0,
    extraBufferCapacity: Int = 1
) : UIEventDispatcher<E> {

    private val _events = MutableSharedFlow<E>(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity
    )

    override val events: SharedFlow<E> = _events.asSharedFlow()

    override fun dispatch(event: E) {
        if (event.canDrop) {
            // 非关键事件：能发就发，不能就算
            _events.tryEmit(event)
        } else {
            // 关键事件：保证送达
            scope.launch {
                _events.emit(event)
            }
        }
    }
}
