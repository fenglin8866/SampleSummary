package com.sample.feature.logger.logs.record

import com.sample.feature.logger.logs.repository.LogRepository
import com.sample.feature.logger.logs.repository.di.ObjectDependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 推荐
 */
object LogRecorder {

    private var isRecordLogs = true

    /*private val dependencies: ObjectDependencies by lazy {
        EntryPointAccessors.fromApplication(
           xxh.getContext().applicationContext,
            ObjectDependencies::class.java
        )
    }*/

    private val dependencies: ObjectDependencies?=null

    private val repository: LogRepository?
        get() = dependencies?.getRepository()

    private val scope = CoroutineScope(Dispatchers.IO)


    fun isRecordLogs(): Boolean {
        return isRecordLogs
    }

    fun recordLog(msg: String, startTime: Long) {
        if (isRecordLogs) {
            val endTime = System.currentTimeMillis()
            scope.launch {
                repository?.addLog(msg, startTime, endTime)
            }
        }
    }

}