package com.sample.feature.logger.logs.domain

import android.content.Context
import android.net.Uri
import com.sample.feature.logger.logs.repository.Log
import com.sample.feature.logger.logs.repository.LoggerRepository
import com.sample.feature.logger.logs.util.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogsUserCase @Inject constructor(
    @param:ApplicationContext
    private val appContext: Context,
    private val fileUtil: FileUtil,
    private val logsRepository: LoggerRepository,
) {

    private var startTime: Long = 0

    fun exportLogs(logs: List<Log>): ExportResult {
        return try {
            fileUtil.exportLogsToPublicDirectory(appContext, logs)
            ExportResult.Success
        } catch (e: Exception) {
            ExportResult.Failed(e.message ?: "Unknown error")
        }
    }

    fun getLogsUri(): Uri {
        return fileUtil.getFileUri()
    }

    fun getAllLogs(): Flow<List<Log>> {
        return logsRepository.getAllLogs()
    }

    fun start() {
        startTime = System.currentTimeMillis()
    }

    suspend fun end() {
        val endTime = System.currentTimeMillis()
        logsRepository.addLog("Test time", startTime, endTime)
    }

    suspend fun clearLogs() {
        logsRepository.removeLogs()
    }
}