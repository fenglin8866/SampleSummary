package com.sample.feature.migration.client.config

import androidx.annotation.Keep
import com.google.gson.Gson

@Keep
data class MigrationConfig(
    val screenModel: Int = ConfigHelper.getScreenModel(),
    val smartReadModel: Boolean = ConfigHelper.getSmartReadModel(),
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }

}