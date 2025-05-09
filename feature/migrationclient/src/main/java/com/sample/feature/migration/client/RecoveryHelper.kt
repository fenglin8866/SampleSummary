package com.sample.feature.migration.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.feature.migration.client.inputhistory.UserInputItem

/**
 * 处理所有数据恢复操作
 */
object RecoveryHelper {

    fun recoveryInputHistory(data: String) {
        val gson = Gson()
        val listType = object : TypeToken<List<UserInputItem>>() {}.type
        val list: List<UserInputItem> = gson.fromJson(data, listType)
        //.......
    }

    fun recoveryConfig(data: String) {

    }

}