package com.sample.feature.migration.client

import android.content.Context

/**
 * 迁移完成后通知remote应用
 * 1、标记状态，表示已经迁移过，之后的迁移请求不再响应
 * 2、删除迁移成功后的资源
 * 3、退出应用
 */
object AidlHelper {

    fun unbind(context: Context){

    }
}