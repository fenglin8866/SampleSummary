package com.xxh.summary

import android.app.Application
import com.xxh.summary.download.data.db.AppDatabase
import com.xxh.summary.download.domain.DownloadManager
import com.xxh.summary.download.sdk.RealAppCenterSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val db = AppDatabase.create(this)

        DownloadManager.init(
            sdk = RealAppCenterSdk(),
            dao = db.downloadDao()
        )
    }
}