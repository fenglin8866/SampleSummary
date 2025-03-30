package com.sample.data.repository.book

import kotlinx.coroutines.flow.Flow


interface BookRepository {
    val observeAllBooks: Flow<List<Book>>

    fun observeBookById(id: Long): Flow<Book>

    suspend fun bookmark(id: Long, isBookmarked: Boolean)
}