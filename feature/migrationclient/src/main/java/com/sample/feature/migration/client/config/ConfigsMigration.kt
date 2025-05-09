package com.sample.feature.migration.client.config

import android.content.Context
import android.database.Cursor
import com.sample.feature.migration.client.base.ContentMigration
import androidx.core.net.toUri
import com.sample.feature.migration.client.MigrationState
import com.sample.feature.migration.client.RecoveryHelper

class ConfigsMigration : ContentMigration() {

    override fun query(context: Context) {
        val uri = "content://com.nubia.provider.configs".toUri()
        query(context, uri)
    }

    override fun getCursorColumnIndex(cursor: Cursor): Int {
        return cursor.getColumnIndex("config")
    }

    override fun recoveryHandle(context: Context, data: String): Boolean {
        try {
            RecoveryHelper.recoveryConfig(data)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    override fun isMigrationComplete(): Boolean {
        return MigrationState.isConfigMigrationComplete()
    }

    override fun completeMigration() {
        MigrationState.completeConfigMigration()
    }
}