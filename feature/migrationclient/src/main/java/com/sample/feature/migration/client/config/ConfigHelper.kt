package com.sample.feature.migration.client.config

import android.content.SharedPreferences
import android.preference.PreferenceManager

object ConfigHelper {

   // private var mPrefs:SharedPreferences=PreferenceManager.getDefaultSharedPreferences()

    fun getScreenModel(): Int {
        return 1
    }

    fun getSmartReadModel(): Boolean {
        return true
    }


}