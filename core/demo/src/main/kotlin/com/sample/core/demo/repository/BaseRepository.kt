package com.sample.core.demo.repository

import com.sample.core.demo.model.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * 基础仓库：
 * - 统一处理异常
 * - 包装成 Flow<ResultState>
 */
open class BaseRepository {

    protected fun <T> request(
        block: suspend () -> T
    ): Flow<ResultState<T>> = flow {
        emit(ResultState.Loading)
        val response = block()
        emit(ResultState.Success(response))
    }.catch { e ->
        emit(ResultState.Error(handleException(e)))
    }

    private fun handleException(throwable: Throwable): Throwable {
        return when (throwable) {
            is IOException -> NetworkException("网络异常", throwable)
            is HttpException -> ApiException("服务器异常(${throwable.code()})", throwable)
            else -> throwable
        }
    }
}

/**
 * 自定义异常
 */
class NetworkException(message: String, cause: Throwable?) : IOException(message, cause)
class ApiException(message: String, cause: Throwable?) : IOException(message, cause)