package com.sample.lib.network.repository

import com.sample.lib.network.dispatcher.RequestDispatcher
import com.sample.lib.network.dispatcher.RequestKey
import com.sample.lib.network.dispatcher.RequestStrategy
import com.sample.lib.network.model.ApiException
import com.sample.lib.network.model.BaseResponse
import com.sample.lib.network.model.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

abstract class BaseRepositoryWithRequestDispatcher {

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
                throw ApiException(response.code, response.message)
            }
            result
        }.onStart { emit(ResultState.Loading) }
            .catch { e ->
                val apiError = e as? ApiException
                emit(ResultState.Error(e, apiError?.code))
            }
    }

}