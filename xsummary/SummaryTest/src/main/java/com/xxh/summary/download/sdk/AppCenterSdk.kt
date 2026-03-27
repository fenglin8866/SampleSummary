package com.xxh.summary.download.sdk

interface AppCenterSdk {

    fun download(
        appId: String,
        url: String,
        callback: DownloadCallback
    )

    fun pause(appId: String)

    fun resume(appId: String)

    fun getDownloadingList(): List<DownloadTask>
}