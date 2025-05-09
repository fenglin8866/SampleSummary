package com.sample.feature.migration.remote

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.google.gson.Gson
import com.sample.feature.migration.remote.data.MigrationRepository
import com.sample.feature.migration.remote.data.config.MigrationConfig
import java.io.File
import kotlin.system.exitProcess

object MigrationManager {

    fun getConfigData(): String {
        return MigrationConfig().toJson()
    }

    fun getInputHistoryData(): String {
        val inputHistory = "xxx"
        return Gson().toJson(inputHistory)
    }

    fun getDB(): SQLiteDatabase? {
        return null
    }

    fun getOfflineWebPageFile(context: Context, name: String): File {
        return File(context.externalCacheDir, "Browser/cachePage/$name")
    }

    fun getDownloadFile(name: String): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            name
        )
    }

    fun migrationFinishHandle() {
        MigrationRepository.completeMigration()
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }

    fun isMigrationComplete(): Boolean {
        return MigrationRepository.isMigrationComplete()
    }

}