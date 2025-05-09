package com.sample.feature.migration.remote.data

import android.content.SharedPreferences
import androidx.core.content.edit

object MigrationRepository {

    private var mPrefs: SharedPreferences? = null

    fun completeMigration() {
        mPrefs?.let {
            mPrefs!!.edit(commit = true) { putBoolean("x", false) }
        }
    }

    fun isMigrationComplete(): Boolean {
       return false
    }
}