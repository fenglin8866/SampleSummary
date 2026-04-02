package com.xxh.summary.net.ext

import com.xxh.summary.net.model.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

fun <T> Flow<ResultState<T>>.onSuccess(block: (T?) -> Unit) = onEach {
    if (it is ResultState.Success) block(it.data)
}

fun <T> Flow<ResultState<T>>.onError(block: (Throwable, Int?) -> Unit) = onEach {
    if (it is ResultState.Error) block(it.error, it.code)
}

fun <T> Flow<ResultState<T>>.onLoading(block: () -> Unit) = onEach {
    if (it is ResultState.Loading) block()
}