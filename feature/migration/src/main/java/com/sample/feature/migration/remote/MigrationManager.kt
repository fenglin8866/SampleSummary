package com.sample.feature.migration.remote

import com.sample.feature.migration.remote.data.MigrationRepository
import kotlin.system.exitProcess

object MigrationManager {

    fun migrationFinishHandle() {
        MigrationRepository.completeMigration()
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(0)
    }

}