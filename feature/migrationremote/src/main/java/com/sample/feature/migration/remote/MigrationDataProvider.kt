package com.sample.feature.migration.remote

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.core.net.toUri
import java.io.File

class MigrationDataProvider : ContentProvider() {

    private var db1: SQLiteDatabase? = null
    private var db2: SQLiteDatabase? = null

    //需要提前加载
    private var configsStr: String? = null

    //输入记录
    private var inputHistoryStr: String? = null

    //迁移结束标记
    private var isCompleteMigration: Boolean? = null

    /**
     * 提前加载资源，防止获取耗时导致访问不了
     */
    override fun onCreate(): Boolean {
        try {
            isCompleteMigration = MigrationManager.isMigrationComplete()
            isCompleteMigration?.let {
                if (!it) {
                    db1 = MigrationManager.getDB()
                    //db2= DatabaseHelper.getDatabaseHelper(context).readableDatabase
                    configsStr = MigrationManager.getConfigData()
                    inputHistoryStr = MigrationManager.getInputHistoryData()
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
                    val files = dir.listFiles()
                    if (files != null) {
                        return queryList("filename", files.map {
                            it.name
                        })
                    }
                }
            } else if (matcher.match(uri) == CODE_CONFIG && context != null) {
                return queryString("config", configsStr)
            } else if (matcher.match(uri) == CODE_INPUT_HISTORY && context != null) {
                return queryString("inputHistory", inputHistoryStr)
            } else {
                return queryDB(uri, projection, selection, selectionArgs, null)
            }
        }
        return null
    }

    private fun queryList(columnNames: String, contents: List<String>): Cursor? {
        if (contents.isNotEmpty()) {
            val cursor = MatrixCursor(arrayOf(columnNames))
            contents.forEach {
                cursor.addRow(arrayOf(it))
            }
            return cursor
        } else {
            return null
        }
    }

    private fun queryString(columnNames: String, content: String?): Cursor? {
        if (content.isNullOrEmpty()) {
            return null
        } else {
            val cursor = MatrixCursor(arrayOf(columnNames))
            cursor.addRow(arrayOf(content))
            return cursor
        }
    }

    private fun queryDB(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val table = getTableName(uri)
        val db = getDB(uri)
        return if (table != null && db != null) {
            db.query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
        } else {
            null
        }
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        if (!isMigrationComplete()) {
            val fileName = uri.lastPathSegment
            if (context != null && !fileName.isNullOrEmpty()) {
                val file = dispatchFilePatch(uri, fileName)
                if (file?.exists() == true) {
                    return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                }
            }
        }
        return null
    }

    private fun dispatchFilePatch(uri: Uri, fileName: String): File? {
        return if (matcher.match(uri) == CODE_CACHE_PAGE_ITEM) {
            MigrationManager.getOfflineWebPageFile(context!!, fileName)
        } else if (matcher.match(uri) == CODE_CACHE_DOWNLOAD_ITEM) {
            MigrationManager.getDownloadFile(fileName)
        } else {
            null
        }
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