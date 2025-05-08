package com.sample.feature.migration.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.sample.feature.migration.client.config.ConfigsMigration
import com.sample.feature.migration.client.webpage.OfflineWebPageMigration

class MigrationJobService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {

        wakeUpRemoteApp()
        //拉活activity有一个过程，如果不延时，会导致ContentProvider访问没有权限。
        Thread.sleep(500)
        val controller = MigrationController()
        controller.addMigrationTask(OfflineWebPageMigration())
        controller.addMigrationTask(ConfigsMigration())
        controller.startMigrationTasks(this)
        try {
            if (!MigrationState.isAllMigrationComplete()) {
                if (MigrationState.isDataMigrationErrorTryComplete()) {
                    MigrationState.completeAllMigration()
                } else {
                    MigrationState.setDataMigrationErrorTryNum()
                }
            } else {
                //如果不延时，会导致AIDL异常
                Thread.sleep(5000)
                migrationFinishCallback()
                Thread.sleep(1000)
                AidlHelper.unbind(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun migrationFinishCallback() {

    }


    private fun wakeUpRemoteApp() {
        try {
            val intent = Intent()
            intent.setComponent(
                ComponentName(
                    "com.nubia.nubrowser",
                    "com.sample.feature.migration.remote.MigrationActivity"
                )
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        AidlHelper.unbind(this)
        super.onDestroy()
    }

    companion object {
        private const val JOB_ID = 1001
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MigrationJobService::class.java, JOB_ID, work)
        }
    }

}