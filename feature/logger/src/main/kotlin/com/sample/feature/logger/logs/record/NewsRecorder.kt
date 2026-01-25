package com.sample.feature.logger.logs.record

object NewsRecorder: AbstractRecorder() {
    override fun msg(): String {
        return "News request"
    }
}