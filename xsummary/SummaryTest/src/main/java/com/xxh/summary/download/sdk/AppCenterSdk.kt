package com.xxh.summary.download.sdk

import android.content.Context

interface AppCenterSdk {

    fun initSdk(context: Context)

    fun download(
        packageName: String,
        callback: DownloadCallback
    )

    fun pause(packageName: String)

    fun resume(packageName: String)

    fun getDownloadingList(): List<DownloadTask>
}