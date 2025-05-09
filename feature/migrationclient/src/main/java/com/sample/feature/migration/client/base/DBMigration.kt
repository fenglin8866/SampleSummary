package com.sample.feature.migration.client.base

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * 如何避免迁移异常，重新迁移时不会出现重复的信息
 * 方式1:使用数据库的事物，出现异常，数据库不会存储数据
 * 方式2:使用主键插入，存在的重新插入
 * 方式3:遍历数据库信息，比较过滤。
 */
abstract class DBMigration(
    private val db: SQLiteDatabase,
    private val tableName: String
) : BaseMigration() {
    override fun cursorHandle(context: Context, cursor: Cursor?) {
        if (cursor != null) {
            val contents: MutableList<ContentValues> = ArrayList()
            while (cursor.moveToNext()) {
                val contentValues = generateContentValues(cursor)
                if (contentValues != null) {
                    contents.add(contentValues)
                }
            }
            cursor.close()
            addDB(contents)
        } else {
            completeMigration()
        }
    }

    abstract fun generateContentValues(cursor: Cursor): ContentValues?

    private fun addDB(data: List<ContentValues>) {
        db.beginTransaction()
        try {
            data.forEach {
                db.insert(tableName, null, it)
            }
            db.setTransactionSuccessful()
            completeMigration()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

}