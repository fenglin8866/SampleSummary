package com.xxh.summary.download.data.sdk

import android.content.Context
import com.xxh.summary.download.model.DownloadState

interface AppCenterSdk {

    fun initSdk(context: Context, callback: ((Boolean) -> Unit)? = null)

    fun download(
        packageName: String,
        callback: DownloadCallback
    )

    fun pause(packageName: String, callback: (DownloadState) -> Unit)

    fun resume(packageName: String, callback: (DownloadState) -> Unit)

    fun open(packageName: String)

    fun queryDownloadStatus(packageName: String, callback: (DownloadState?) -> Unit)

    fun registerCallback(packageName: String, progress: Int, callback: DownloadCallback)

    fun isDownloadEnabled(): Boolean
}