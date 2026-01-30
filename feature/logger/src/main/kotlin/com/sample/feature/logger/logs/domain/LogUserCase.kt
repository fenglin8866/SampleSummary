package com.sample.feature.logger.logs.domain

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import com.sample.feature.logger.logs.repository.Log
import com.sample.feature.logger.logs.repository.LogRepository
import com.sample.feature.logger.logs.util.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogUserCase @Inject constructor(
    @param:ApplicationContext
    private val appContext: Context,
    private val fileUtil: FileUtil,
    private val logRepository: LogRepository,
) {

    private var startTime: Long = 0

    @WorkerThread
    fun exportAllLogs(logs: List<Log>): ExportResult {
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
        return logRepository.getAllLogs()
    }

    suspend fun clearAllLogs() {
        logRepository.removeLogs()
    }

    fun start() {
        startTime = System.currentTimeMillis()
    }

    suspend fun end() {
        if (startTime == 0L) return
        val endTime = System.currentTimeMillis()
        logRepository.addLog("Test time", startTime, endTime)
        startTime = 0
    }

}