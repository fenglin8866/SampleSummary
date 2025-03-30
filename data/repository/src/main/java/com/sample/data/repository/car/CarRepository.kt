package com.sample.data.repository.car

import kotlinx.coroutines.flow.Flow

interface CarRepository {
    val carNames: Flow<List<String>>

    suspend fun add(name: String)
}