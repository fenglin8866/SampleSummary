package com.xxh.summary.net.repository

import com.xxh.summary.net.dispatcher.RequestDispatcher
import com.xxh.summary.net.dispatcher.RequestKey
import com.xxh.summary.net.dispatcher.RequestStrategy
import com.xxh.summary.net.model.ApiException
import com.xxh.summary.net.model.BaseResponse
import com.xxh.summary.net.model.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

abstract class RequestDispatcherBaseRepository {

    fun <T> request(
        scope: CoroutineScope,
        key: RequestKey,
        strategy: RequestStrategy = RequestStrategy.NORMAL,
        block: suspend () -> BaseResponse<T>
    ): Flow<ResultState<T>> {

        return RequestDispatcher.execute(
            scope = scope,
            key = key,
            strategy = strategy
        ) {
            block()
        }.map { response ->
            val result: ResultState<T> = if (response.code == 0) {
                ResultState.Success(response.data)
            } else {
                throw ApiException(response.code, response.message.toString())
            }
            result
        }.onStart { emit(ResultState.Loading) }
            .catch { e ->
                val apiError = e as? ApiException
                emit(ResultState.Error(e, apiError?.code))
            }
    }

}