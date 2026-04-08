package com.xxh.summary.download.sdk

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

    fun queryDownloadStatus(packageName: String): DownloadState?

    fun registerCallback(state: DownloadState, callback: DownloadCallback)
}