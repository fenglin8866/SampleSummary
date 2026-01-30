package com.sample.feature.logger.logs.repository


import com.sample.feature.logger.logs.repository.data.LogDataSource
import com.sample.feature.logger.logs.repository.di.Database
import com.sample.feature.logger.logs.repository.di.InMemory
import com.sample.feature.logger.logs.util.DateFormatter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(@param:Database private val source: LogDataSource) {

    /*private val _logs = MutableStateFlow<List<Log>>(emptyList())

    val logs: StateFlow<List<Log>> =_logs

    init {

        getAllLogs().collect {
            _logs.value = it
        }
    }*/

    fun getAllLogs(): Flow<List<Log>> {
        return source.getAllLogs()
    }

    suspend fun addLog(msg: String, startTime: Long, endTime: Long) {
        val startTimeStr = DateFormatter.formatDate(startTime)
        val endTimeStr = DateFormatter.formatDate(endTime)
        val log = Log(msg, startTime, endTime, startTimeStr, endTimeStr)
        source.addLog(log)
    }

    suspend fun removeLogs() {
        source.removeLogs()
    }
}