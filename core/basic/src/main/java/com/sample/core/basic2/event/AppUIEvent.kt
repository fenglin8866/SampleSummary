package com.sample.core.basic2.event

sealed interface AppUIEvent : UIEvent {

    // ---------- 不能丢 ----------
    sealed interface Critical : AppUIEvent {
        override val canDrop: Boolean get() = false
    }

    // ---------- 可以丢 ----------
    sealed interface NonCritical : AppUIEvent {
        override val canDrop: Boolean get() = true
    }
}
