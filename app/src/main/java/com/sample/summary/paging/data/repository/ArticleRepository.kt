package com.sample.summary.paging.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sample.summary.paging.data.api.ApiService
import com.sample.summary.paging.data.model.Article
import com.sample.summary.paging.data.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getArticlePagingFlow(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ArticlePagingSource(apiService) }
        ).flow
    }
}
