package com.sample.core.basic2.view

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import android.view.View

/**
 * 每次点这个 View（比如按钮），就往 Flow 里发一个 Unit；
 * 页面销毁或者取消监听时，清除 setOnClickListener。
 */
fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}
