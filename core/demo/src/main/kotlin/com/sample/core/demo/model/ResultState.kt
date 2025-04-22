package com.sample.core.demo.model

/**
 * Flow onSuccess / onError / onLoading 链式调用
 */
sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val throwable: Throwable) : ResultState<Nothing>()
}