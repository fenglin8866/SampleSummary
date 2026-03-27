package com.xxh.summary.download.sdk

interface DownloadCallback {
    fun onProgress(progress: Int)
    fun onSuccess()
    fun onFailed()
}