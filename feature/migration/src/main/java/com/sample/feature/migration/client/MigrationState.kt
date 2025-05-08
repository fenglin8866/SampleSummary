package com.sample.feature.migration.client

/**
 * 迁移状态管理
 */
object MigrationState {

    fun isConfigMigrationComplete(): Boolean {
        return true
    }

    fun completeConfigMigration() {

    }

    fun isInputHistoryMigrationComplete(): Boolean {
        return true
    }

    fun completeInputHistoryMigration() {

    }

    fun isHistoryMigrationComplete(): Boolean {
        return true
    }

    fun completeHistoryMigration() {

    }

    fun isOfflineWebpageMigrationComplete(): Boolean {
        return true
    }

    fun completeOfflineWebpageMigration() {

    }

    private fun getDataMigrationErrorTryNum(): Long {
        return 1
    }

    fun setDataMigrationErrorTryNum() {
        val newNum = getDataMigrationErrorTryNum() + 1
        //sp持久化
    }

    fun isDataMigrationErrorTryComplete(): Boolean {
        return getDataMigrationErrorTryNum() > 4
    }


    fun isAllMigrationComplete(): Boolean {
        return isConfigMigrationComplete() &&
                isInputHistoryMigrationComplete() &&
                isHistoryMigrationComplete() &&
                isOfflineWebpageMigrationComplete()
    }

    /**
     * 包含两种情况
     * 1、顺利迁移完成
     * 2、迁移异常并超过重试次数
     */
    fun completeAllMigration() {
        completeConfigMigration()
        completeHistoryMigration()
        completeInputHistoryMigration()
        completeOfflineWebpageMigration()
    }

}