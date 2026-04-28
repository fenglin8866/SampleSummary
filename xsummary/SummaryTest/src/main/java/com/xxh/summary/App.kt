package com.xxh.summary

import android.app.Application
import com.sample.lib.network.util.NetLog
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        NetLog.setEnable(true)
    }
}