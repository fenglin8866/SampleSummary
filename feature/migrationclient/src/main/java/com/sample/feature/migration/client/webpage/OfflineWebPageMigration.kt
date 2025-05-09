package com.sample.feature.migration.client.webpage

import android.content.Context
import android.database.Cursor
import androidx.core.net.toUri
import com.sample.feature.migration.client.MigrationState
import com.sample.feature.migration.client.Util
import com.sample.feature.migration.client.base.ContentMigration
import com.sample.feature.migration.client.base.FileMigration
import java.io.File

class OfflineWebPageMigration : ContentMigration() {

    override fun query(context: Context) {
        val uri = "content://com.nubia.provider.cachePage".toUri()
        query(context, uri)
    }

    override fun getCursorColumnIndex(cursor: Cursor): Int {
        return cursor.getColumnIndex("filename")
    }

    override fun recoveryHandle(context: Context, data: String): Boolean {
        return migrationOfflineItem(context, data)
    }

    private fun migrationOfflineItem(context: Context, fileName: String): Boolean {
        val uri = "content://com.nubia.provider.cachePage/$fileName".toUri()
        val targetFile = File(Util.getSavePath(), fileName)
        return FileMigration.startMigration(context, uri, targetFile)
    }


    override fun isMigrationComplete(): Boolean {
        return MigrationState.isOfflineWebpageMigrationComplete()
    }

    override fun completeMigration() {
        MigrationState.completeOfflineWebpageMigration()
    }
}