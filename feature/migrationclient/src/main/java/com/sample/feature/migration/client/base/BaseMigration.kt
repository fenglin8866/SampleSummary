package com.sample.feature.migration.client.base

import android.content.Context
import android.database.Cursor
import android.net.Uri

abstract class BaseMigration : IMigration {

    override fun startMigration(context: Context) {
        if (!isMigrationComplete()) {
            query(context)
        }
    }

    fun query(context: Context, uri: Uri) {
        val cursor = context.contentResolver.query(uri, null, null, null, null, null)
        cursorHandle(context, cursor)
    }

    abstract fun isMigrationComplete(): Boolean

    abstract fun completeMigration()

    abstract fun query(context: Context)

    abstract fun cursorHandle(context: Context, cursor: Cursor?)
}