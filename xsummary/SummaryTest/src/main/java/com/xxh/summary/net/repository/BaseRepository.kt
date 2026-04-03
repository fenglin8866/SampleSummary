package com.xxh.summary.net.repository

import com.xxh.summary.net.model.ApiException
import com.xxh.summary.net.model.BaseResponse
import com.xxh.summary.net.model.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

abstract class BaseRepository {

    fun <T> request(
        block: suspend () -> BaseResponse<T>
    ): Flow<ResultState<T>> = flow {

        emit(ResultState.Loading)

        val response = block()

        if (response.code == 0) {
            emit(ResultState.Success(response.data))
        } else {
            throw ApiException(response.code, response.message.toString())
        }

    }.catch { e ->
        val apiError = e as? ApiException

        emit(
            ResultState.Error(
                error = e,
                code = apiError?.code
            )
        )
    }

}