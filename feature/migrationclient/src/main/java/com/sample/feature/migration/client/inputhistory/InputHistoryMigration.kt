package com.sample.feature.migration.client.inputhistory

import android.content.Context
import android.database.Cursor
import androidx.core.net.toUri
import com.sample.feature.migration.client.MigrationState
import com.sample.feature.migration.client.RecoveryHelper
import com.sample.feature.migration.client.base.ContentMigration

class InputHistoryMigration : ContentMigration() {

    override fun query(context: Context) {
        val uri = "content://com.nubia.provider.inputHistory".toUri()
        query(context, uri)
    }

    override fun getCursorColumnIndex(cursor: Cursor): Int {
        return cursor.getColumnIndex("inputHistory")
    }

    override fun recoveryHandle(context: Context, data: String): Boolean {
        try {
            RecoveryHelper.recoveryInputHistory(data)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    override fun isMigrationComplete(): Boolean {
        return MigrationState.isInputHistoryMigrationComplete()
    }

    override fun completeMigration() {
        MigrationState.completeInputHistoryMigration()
    }
}