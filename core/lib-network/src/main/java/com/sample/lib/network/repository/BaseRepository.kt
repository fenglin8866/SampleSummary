package com.sample.lib.network.repository

import com.sample.lib.network.model.ApiException
import com.sample.lib.network.model.BaseResponse
import com.sample.lib.network.model.ResultState
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
            throw ApiException(response.code, response.message)
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