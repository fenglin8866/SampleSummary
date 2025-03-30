package com.sample.data.repository.book

import kotlinx.coroutines.flow.Flow

interface BookRepository {
    val bookNames: Flow<List<String>>

    suspend fun add(name: String)
}