package com.sample.core.demo.repository

import androidx.paging.*
import kotlinx.coroutines.flow.Flow

/**
 * 基础分页仓库
 */
open class BasePagingRepository {

    protected fun <Value : Any> simplePager(
        pageSize: Int = 20,
        enablePlaceholders: Boolean = false,
        pagingSourceFactory: () -> PagingSource<Int, Value>
    ): Flow<PagingData<Value>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = enablePlaceholders
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}