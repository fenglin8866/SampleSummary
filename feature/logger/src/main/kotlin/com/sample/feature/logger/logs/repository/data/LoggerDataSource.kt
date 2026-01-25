package com.sample.feature.logger.logs.repository.data

import com.sample.feature.logger.logs.repository.Log
import kotlinx.coroutines.flow.Flow

interface LoggerDataSource {
    suspend fun addLog(log: Log)

    fun getAllLogs(callback: (List<Log>) -> Unit)

    suspend fun removeLogs()

    fun getAllLogs(): Flow<List<Log>>
}