package com.sample.core.basic.ui.utils

import kotlinx.coroutines.flow.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * 防抖（debounce）点击流
 */
fun <T> Flow<T>.debounceClick(duration: Duration = 500.milliseconds): Flow<T> {
    return this.debounce(duration)
}

/**
 * 节流（throttleFirst）点击流
 * —— 500ms 内只取第一个元素
 */
fun <T> Flow<T>.throttleFirst(windowDuration: Duration = 500.milliseconds): Flow<T> = channelFlow {
    var lastEmissionTime = 0L

    collect { value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime >= windowDuration.inWholeMilliseconds) {
            lastEmissionTime = currentTime
            send(value)
        }
    }
}

/**
 * 带 try-catch 捕获异常的安全收集
 */
suspend fun <T> Flow<T>.safeCollect(
    onError: (Throwable) -> Unit = {},
    collector: suspend (T) -> Unit
) {
    try {
        collect(collector)
    } catch (e: Exception) {
        onError(e)
    }
}

/**
 * 仅第一次触发（比如某些初始化意图）
 */
fun <T> Flow<T>.takeFirst(): Flow<T> {
    return this.take(1)
}

/*
使用示例
val submitIntentFlow = binding.submitButton.clicks()
    .throttleFirst(500.milliseconds)
    .map { MainUIIntent.SubmitOrder("123") }

val refreshIntentFlow = binding.swipeRefreshLayout.refreshes()
    .debounceClick(800.milliseconds)
    .map { MainUIIntent.RefreshData }

viewModel.consumeIntents(
merge(submitIntentFlow, refreshIntentFlow)
)
*/

