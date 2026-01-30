package com.sample.feature.logger.logs.record

object NewsLogRecorder: AbstractRecorder() {
    override fun msg(): String {
        return "News request"
    }
}