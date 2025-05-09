package com.sample.feature.migration.client.base

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileMigration {

    fun startMigration(context: Context, uri: Uri, file: File): Boolean {
        if (!file.exists()) {
            try {
                context.contentResolver.openInputStream(uri).use { `in` ->
                    FileOutputStream(file).use { out ->
                        if (`in` != null) {
                            val buffer = ByteArray(4096)
                            var len: Int
                            while ((`in`.read(buffer).also { len = it }) != -1) {
                                out.write(buffer, 0, len)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //出现异常，必须删除文件，防止下次因为文件存在跳过
                file.delete()
                return false
            }
        }
        return true
    }

}
