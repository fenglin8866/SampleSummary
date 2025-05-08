package com.sample.feature.migration.remote

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import com.google.gson.Gson
import com.sample.feature.migration.remote.data.MigrationRepository
import com.sample.feature.migration.remote.data.config.MigrationConfig
import androidx.core.net.toUri
import java.io.File

class MigrationDataProvider : ContentProvider() {
    private var db1: SQLiteDatabase? = null
    private var db2: SQLiteDatabase? = null

    //需要提前加载
    private var configsStr: String? = null
    private var inputHistoryStr: String? = null
    private var isCompleteMigration: Boolean? = null


    override fun onCreate(): Boolean {
        try {
            isCompleteMigration = MigrationRepository.isMigrationComplete()
            isCompleteMigration?.let {
                if (!it) {
                    //db1= DatabaseHelper.getDatabaseHelper(context).readableDatabase
                    //db2= DatabaseHelper.getDatabaseHelper(context).readableDatabase
                    configsStr = MigrationConfig().toJson()
                    //  val inputHistory = DataCenter.getInstance().userInputItemsFromDB
                    val inputHistory = "xxx"
                    inputHistoryStr = Gson().toJson(inputHistory)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (!isMigrationComplete()) {
            if (matcher.match(uri) == CODE_CACHE_PAGE && context != null) {
                val dir = File(context!!.externalCacheDir, "Browser/cachePage")
                if (dir.exists()) {
                    val files = dir.listFiles() ?: return null
                    val cursor = MatrixCursor(arrayOf("filename"))
                    for (file in files) {
                        cursor.addRow(arrayOf(file.name))
                    }
                    return cursor
                }
            } else if (matcher.match(uri) == CODE_CONFIG && context != null) {
                val cursor = MatrixCursor(arrayOf("config"))
                cursor.addRow(arrayOf(configsStr))
                return cursor
            } else if (matcher.match(uri) == CODE_INPUT_HISTORY && context != null) {
                val cursor = MatrixCursor(arrayOf("inputHistory"))
                cursor.addRow(arrayOf(inputHistoryStr))
                return cursor
            } else {
                val table = getTableName(uri)
                val db = getDB(uri)
                if (table != null && db != null) {
                    return db.query(
                        table,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                    )
                }
            }
        }
        return null
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        if (!isMigrationComplete()) {
            val fileName = uri.lastPathSegment
            if (context != null && !fileName.isNullOrEmpty()) {
                val file = if (matcher.match(uri) == CODE_CACHE_PAGE_ITEM) {
                    File(context!!.externalCacheDir, "Browser/cachePage/$fileName")
                } else if (matcher.match(uri) == CODE_CACHE_DOWNLOAD_ITEM) {
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        fileName
                    )
                } else {
                    null
                }
                if (file?.exists() == true) {
                    return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                }
            }
        }
        return null
    }


    override fun getType(uri: Uri): String? {
        return when (matcher.match(uri)) {
            CODE_BOOKMARKS -> "vnd.android.cursor.dir/vnd.com.nubia.provider.bookmarks"
            CODE_HISTORY -> "vnd.android.cursor.dir/vnd.com.nubia.provider.history"
            CODE_DOWNLOAD -> "vnd.android.cursor.dir/vnd.com.nubia.provider.download"
            CODE_DOWNLOAD_REQUEST -> "vnd.android.cursor.dir/vnd.com.nubia.provider.download_request"
            CODE_CACHE_PAGE, CODE_CACHE_PAGE_ITEM -> "application/octect-stream"
            else -> null
        }

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }


    private fun isMigrationComplete(): Boolean {
        return isCompleteMigration ?: false
    }


    private fun getTableName(uri: Uri): String? {
        return when (matcher.match(uri)) {
            CODE_BOOKMARKS -> "bookmarks"
            CODE_HISTORY -> "history"
            CODE_DOWNLOAD -> "download_table"
            CODE_DOWNLOAD_REQUEST -> "download_request_header_table"
            else -> null
        }
    }

    private fun getDB(uri: Uri): SQLiteDatabase? {
        return when (matcher.match(uri)) {
            CODE_BOOKMARKS, CODE_HISTORY -> db1
            CODE_DOWNLOAD, CODE_DOWNLOAD_REQUEST -> db2
            else -> null
        }
    }


    companion object {
        private const val AUTHORITY: String = "com.nubia.provider"
        val BOOKMARKS_URI: Uri = "content://$AUTHORITY/bookmarks".toUri()
        val HISTORY_URI: Uri = "content://$AUTHORITY/history".toUri()
        val DOWNLOAD_URI: Uri = "content://$AUTHORITY/download".toUri()
        val CACHE_PAGE: Uri = "content://$AUTHORITY/cachePage".toUri()
        val DOWNLOAD_REQUEST_URI: Uri = "content://$AUTHORITY/downloadRequest".toUri()

        private const val CODE_BOOKMARKS = 1
        private const val CODE_HISTORY = 2
        private const val CODE_DOWNLOAD = 3
        private const val CODE_DOWNLOAD_REQUEST = 4
        private const val CODE_CACHE_PAGE = 5
        private const val CODE_CACHE_PAGE_ITEM = 6
        private const val CODE_CONFIG = 7
        private const val CODE_INPUT_HISTORY = 8
        private const val CODE_CACHE_DOWNLOAD_ITEM = 9

        private val matcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            matcher.addURI(AUTHORITY, "bookmarks", CODE_BOOKMARKS)
            matcher.addURI(AUTHORITY, "history", CODE_HISTORY)
            matcher.addURI(AUTHORITY, "download", CODE_DOWNLOAD)
            matcher.addURI(AUTHORITY, "download_request", CODE_DOWNLOAD_REQUEST)
            matcher.addURI(AUTHORITY, "cachePage", CODE_CACHE_PAGE)
            matcher.addURI(AUTHORITY, "cachePage/*", CODE_CACHE_PAGE_ITEM)
            matcher.addURI(AUTHORITY, "configs", CODE_CONFIG)
            matcher.addURI(AUTHORITY, "inputHistory", CODE_INPUT_HISTORY)
            matcher.addURI(AUTHORITY, "downloads/*", CODE_CACHE_DOWNLOAD_ITEM)
        }
    }
}