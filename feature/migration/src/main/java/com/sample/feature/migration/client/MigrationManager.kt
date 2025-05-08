package com.sample.feature.migration.client

import android.content.Context
import android.content.Intent

/**
 * 对外接口，执行数据迁移
 */
object MigrationManager {

    fun onMigrationData(context: Context) {
        if (isMigration()) {
            val intent = Intent(context, MigrationJobService::class.java)
            MigrationJobService.enqueueWork(context, intent)
        }
    }

    /**
     * 迁移条件判断
     * 1、特定机型，特别ROM等等
     * 2、是否迁移完成
     */
    private fun isMigration(): Boolean {
        return !MigrationState.isAllMigrationComplete()
    }

}