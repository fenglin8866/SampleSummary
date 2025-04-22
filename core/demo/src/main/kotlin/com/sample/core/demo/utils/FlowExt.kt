package com.sample.core.demo.utils

import com.sample.core.demo.model.ResultState
import kotlinx.coroutines.flow.*

/**
 * Flow 防抖（debounce）扩展
 */
fun <T> Flow<T>.debounceExt(timeMillis: Long = 300): Flow<T> =
    debounce(timeMillis)

/**
 * Flow 节流（throttleFirst）扩展
 */
fun <T> Flow<T>.throttleFirst(windowDuration: Long = 500): Flow<T> = flow {
    var lastTime = 0L
    collect { value ->
        val current = System.currentTimeMillis()
        if (current - lastTime >= windowDuration) {
            lastTime = current
            emit(value)
        }
    }
}

/**
 * Flow 异常捕获 + 打日志
 */
fun <T> Flow<T>.catchLog(): Flow<T> =
    catch { e ->
        CrashReporter.report(e)
        throw e
    }


fun <T> Flow<ResultState<T>>.onSuccess(action: suspend (T) -> Unit): Flow<ResultState<T>> =
    onEach { state ->
        if (state is ResultState.Success) {
            action(state.data)
        }
    }

fun <T> Flow<ResultState<T>>.onError(action: suspend (Throwable) -> Unit): Flow<ResultState<T>> =
    onEach { state ->
        if (state is ResultState.Error) {
            action(state.throwable)
        }
    }

fun <T> Flow<ResultState<T>>.onLoading(action: suspend () -> Unit): Flow<ResultState<T>> =
    onEach { state ->
        if (state is ResultState.Loading) {
            action()
        }
    }