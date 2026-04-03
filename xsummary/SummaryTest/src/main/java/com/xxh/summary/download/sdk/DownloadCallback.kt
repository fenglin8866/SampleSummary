package com.xxh.summary.download.sdk

import com.xxh.summary.download.model.Status

interface DownloadCallback {
    fun onProgress(progress: Int)

    fun onStatusChanged(status: Status)
}