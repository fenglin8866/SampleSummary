package com.xxh.summary.download.data.model

data class DownloadState(
    val appId: String,
    val progress: Int = 0,
    val status: Status = Status.IDLE
)

enum class Status {
    IDLE,
    DOWNLOADING,
    PAUSED,
    SUCCESS,
    FAILED
}