package com.sample.feature.migration.remote

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MigrationFinishService : Service() {

    private val binder = object : IMigrationAidlInterface.Stub() {
        override fun onFinish() {
            MigrationManager.migrationFinishHandle()
        }
    }


    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}