package com.sample.core.basic.mvi

class PrioritizedIntent<I>(
    val intent: I,
    val priority: Int = 0
)