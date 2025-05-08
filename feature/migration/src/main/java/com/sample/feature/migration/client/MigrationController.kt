package com.sample.feature.migration.client

import android.content.Context
import com.sample.feature.migration.client.base.IMigration

class MigrationController {

    private val migrationList: MutableList<IMigration> = mutableListOf()


    fun addMigrationTask(migration: IMigration) {
        migrationList.add(migration)
    }

    fun clear() {
        migrationList.clear()
    }

    fun startMigrationTasks(context: Context) {
        migrationList.forEach {
            //一个迁移任务异常，不会影响其他迁移流程的进行
            try {
                it.startMigration(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}