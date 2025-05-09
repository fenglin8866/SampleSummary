package com.sample.core.basic.mvi

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun View.clicks() = callbackFlow{
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose {
        setOnClickListener(null)
    }
}