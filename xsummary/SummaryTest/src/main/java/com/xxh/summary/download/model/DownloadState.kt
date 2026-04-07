package com.xxh.summary.download.model

data class DownloadState(
    val packageName: String,
    val progress: Int = 0,
    val status: Status = Status.IDLE,
    val error: String = ""
)

enum class Status {
    IDLE,
    DOWNLOADING,
    PAUSED,
    SUCCESS,
    FAILED,
    INSTALLING,
    INSTALLED,
    UNKNOWN
}