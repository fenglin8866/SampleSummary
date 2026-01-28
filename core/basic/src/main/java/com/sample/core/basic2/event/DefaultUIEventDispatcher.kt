package com.sample.core.basic2.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUIEventDispatcher<E : UIEvent> @Inject constructor(
    private val scope: CoroutineScope,
    replay: Int = 0,
    extraBufferCapacity: Int = 1
) : UIEventDispatcher<E> {

    private val _events = MutableSharedFlow<E>(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val events: SharedFlow<E> = _events.asSharedFlow()

    override fun dispatch(event: E) {
        if (event.canDrop) {
            // 非阻塞，非关键事件：能发就发，不能就算
            _events.tryEmit(event)
        } else {
            //阻塞，关键事件：保证送达
            scope.launch {
                _events.emit(event)
            }
        }
    }
}