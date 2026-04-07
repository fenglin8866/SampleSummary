package com.xxh.summary

import android.app.Application
import com.xxh.summary.download.data.db.AppDatabase
import com.xxh.summary.download.data.repository.DownloadRepository
import com.xxh.summary.download.sdk.RealAppCenterSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sdk = RealAppCenterSdk()
        // sdk.initSdk(this)
        val db = AppDatabase.create(this)
        DownloadRepository.init(
            context = this,
            sdk = sdk,
            dao = db.downloadDao()
        )
    }
}