package com.sample.core.basic.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel : ViewModel() {

    protected val uiEventDispatcher =
        DefaultUIEventDispatcher<UIEvent>(
            scope = viewModelScope
        )

    val uiEvents: SharedFlow<UIEvent>
        get() = uiEventDispatcher.events
}
