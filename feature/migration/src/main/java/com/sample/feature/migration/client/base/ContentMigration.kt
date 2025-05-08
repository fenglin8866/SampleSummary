package com.sample.feature.migration.client.base

import android.content.Context
import android.database.Cursor

abstract class ContentMigration : BaseMigration() {

    override fun cursorHandle(context: Context, cursor: Cursor?) {
        if (cursor != null) {
            val contents: MutableList<String> = ArrayList()
            val nameIndex = getCursorColumnIndex(cursor)
            while (cursor.moveToNext()) {
                val content = cursor.getString(nameIndex)
                if (!content.isNullOrEmpty()) {
                    contents.add(content)
                }
            }
            cursor.close()
            resultHandle(context, contents)
        } else {
            completeMigration()
        }
    }


    private fun resultHandle(context: Context, data: List<String>) {
        var isComplete = true
        for (name in data) {
            val success = recoveryHandle(context, name)
            if (!success) {
                isComplete = false
            }
        }
        if (isComplete) {
            completeMigration()
        }
    }

    abstract fun getCursorColumnIndex(cursor: Cursor): Int

    abstract fun recoveryHandle(context: Context, data: String): Boolean

}