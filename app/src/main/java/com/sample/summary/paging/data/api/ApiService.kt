package com.sample.summary.paging.data.api

import com.sample.summary.paging.data.model.Article
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    suspend fun getArticles(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ArticleResponse
}

data class ArticleResponse(
    val articles: List<Article>
)
