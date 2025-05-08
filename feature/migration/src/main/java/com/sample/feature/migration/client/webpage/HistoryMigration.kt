package com.sample.feature.migration.client.webpage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.net.toUri
import com.sample.feature.migration.client.MigrationState
import com.sample.feature.migration.client.base.DBMigration

class HistoryMigration(
    db: SQLiteDatabase
) : DBMigration(db, tableName = "history") {

    override fun query(context: Context) {
        val uri = "content://com.nubia.provider.history".toUri()
        query(context, uri)
    }

    override fun isMigrationComplete(): Boolean {
        return MigrationState.isHistoryMigrationComplete()
    }

    override fun completeMigration() {
        MigrationState.completeHistoryMigration()
    }

    @SuppressLint("Range")
    override fun generateContentValues(cursor: Cursor): ContentValues {
        val values = ContentValues()
        values.put("title", cursor.getString(cursor.getColumnIndex("title")))
        values.put("url", cursor.getString(cursor.getColumnIndex("url")))
        values.put("created", cursor.getInt(cursor.getColumnIndex("created")))
        values.put("date", cursor.getLong(cursor.getColumnIndex("date")))
        values.put("visits", cursor.getInt(cursor.getColumnIndex("visits")))
        return values
    }

}