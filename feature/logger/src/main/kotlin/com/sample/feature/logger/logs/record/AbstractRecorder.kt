package com.sample.feature.logger.logs.record


abstract class AbstractRecorder {

    private var startTime: Long = 0

    fun start() {
        startTime = System.currentTimeMillis()
    }

    abstract fun msg(): String

    fun endSuccess() {
        recordLog("${msg()} success")
    }

    fun endFailure() {
        recordLog("${msg()} failure")
    }

    fun endException() {
        recordLog("${msg()} exception")
    }

    private fun recordLog(msg: String) {
        LogRecorder.recordLog(msg, startTime)
    }
}