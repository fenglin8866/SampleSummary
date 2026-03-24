package com.sample.lib.network.model

sealed class ResultState<out T> {

    /** 空状态（初始化 / 重置）
     *  用于 UI 初始化，表示没有数据，避免一开始就 Loading 或空指针。
     * */
    object Idle : ResultState<Nothing>()

    /** 加载中 */
    object Loading : ResultState<Nothing>()

    /** 成功 */
    data class Success<T>(
        val data: T?,
        val isFromCache: Boolean = false
    ) : ResultState<T>()

    /** 失败 */
    data class Error(
        val error: Throwable,
        val code: Int? = null
    ) : ResultState<Nothing>()
}

inline fun <T> ResultState<T>.onSuccess(block: (T?) -> Unit): ResultState<T> {
    if (this is ResultState.Success) block(data)
    return this
}

inline fun <T> ResultState<T>.onError(block: (Throwable, Int?) -> Unit): ResultState<T> {
    if (this is ResultState.Error) block(error, code)
    return this
}

inline fun <T> ResultState<T>.onLoading(block: () -> Unit): ResultState<T> {
    if (this is ResultState.Loading) block()
    return this
}

inline fun <T> ResultState<T>.onIdle(block: () -> Unit): ResultState<T> {
    if (this is ResultState.Idle) block()
    return this
}