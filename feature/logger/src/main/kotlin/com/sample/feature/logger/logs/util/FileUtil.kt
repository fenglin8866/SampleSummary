package com.sample.feature.logger.logs.util

import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.sample.core.basic.ui.utils.LogUtils
import com.sample.feature.logger.logs.repository.Log
import java.io.OutputStream
import javax.inject.Inject

class FileUtil @Inject constructor() {

    fun openFileManager(context: Context, callback: (String) -> Unit) {
        val intent = Intent(Intent.ACTION_RUN)
        var pkg = "zte.com.cn.filer"
        var cls = "zte.com.cn.filer.DirActivity"
        val fileParent =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        intent.putExtra("dir_path_init", fileParent)
        try {
            jumpFileDir(context, intent, pkg, cls)
        } catch (e: Exception) {
            try {
                pkg = "cn.nubia.myfile"
                intent.putExtra("set_folder_path", fileParent)
                jumpFileDir(context, intent, pkg, cls)
            } catch (e2: Exception) {
                LogUtils.e("DownloadSettingActivity", "openFileManager err" + e2.message)
                try {
                    pkg = "cn.nubia.myfile"
                    cls = "cn.nubia.myfile.CategoryActivity"
                    intent.putExtra("set_folder_path", fileParent)
                    jumpFileDir(context, intent, pkg, cls)
                } catch (e3: Exception) {
                    callback("Failed to open FileManager")
                }
            }
        }
    }

    fun exportLogsToPublicDirectory(
        context: Context,
        logs: List<Log>
    ) {
        val time = DateFormatter.formatDate(System.currentTimeMillis())
        // 使用 MediaStore API 创建文件
        val contentValues = ContentValues().apply {
            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                getFileName(time)
            )
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_DOCUMENTS}/Browser/"
            )
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { outputStream ->
                writeLogsToStream(outputStream, time, logs)
            }
        } else {
            throw Exception("Failed to create export file")
        }
    }

    // 将日志写入输出流
    private fun writeLogsToStream(outputStream: OutputStream, time: String, logs: List<Log>) {
        outputStream.writer().use { writer ->
            writer.appendLine("Nubia Browser Logs Export")
            writer.appendLine(
                "Export Time: $time"
            )
            writer.appendLine("".padStart(50, '='))

            logs.forEach { log ->
                writer.appendLine("   Action: ${log.msg}")
                writer.appendLine("StartTime: ${log.startTimeStr}")
                writer.appendLine("  EndTime: ${log.endTimeStr}")
                writer.appendLine(" Duration: ${log.duration}ms")
                writer.appendLine("".padStart(40, '-'))
            }
        }
    }

    private fun getFileName(time: String): String {
        return "browser_logs_${time}.txt"
    }


    fun getFileUri(): Uri {
        return DocumentsContract.buildDocumentUri(
            "com.android.externalstorage.documents",
            "${Environment.DIRECTORY_DOCUMENTS}/Browser"
        )
    }

    private fun jumpFileDir(context: Context, intent: Intent, pkg: String, cls: String) {
        intent.component = ComponentName(pkg, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}