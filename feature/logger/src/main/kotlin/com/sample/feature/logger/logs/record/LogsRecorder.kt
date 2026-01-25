package com.sample.feature.logger.logs.record


import com.sample.feature.logger.logs.repository.LoggerRepository
import com.sample.feature.logger.logs.repository.di.ObjectDependencies
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LogsRecorder {

    private var isRecordLogs = true

    private val dependencies: ObjectDependencies by lazy {
        EntryPointAccessors.fromApplication(
            Browser.getContext().applicationContext,
            ObjectDependencies::class.java
        )
    }

    private val repository: LoggerRepository
        get() = dependencies.getRepository()

    private val scope = CoroutineScope(Dispatchers.IO)


    fun isRecordLogs(): Boolean {
        return isRecordLogs
    }

    fun recordLog(msg: String, startTime: Long) {
        if (isRecordLogs) {
            val endTime = System.currentTimeMillis()
            scope.launch {
                repository.addLog(msg, startTime, endTime)
            }
        }
    }

}