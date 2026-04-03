package com.xxh.summary.download.ui.adapter

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.nubia.ns_lib.download.DownloadStatus
import com.xxh.summary.R
import com.xxh.summary.download.model.AppInfo
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivAppIcon: ImageView = itemView.findViewById(R.id.iv_app_icon)
    private val tvAppName: TextView = itemView.findViewById(R.id.tv_app_name)
    private val tvPackageName: TextView = itemView.findViewById(R.id.tv_package_name)
    private val tvDownloadCount: TextView = itemView.findViewById(R.id.tv_download_count)
    private val layoutDownloadInfo: LinearLayout = itemView.findViewById(R.id.layout_download_info)
    private val progressDownload: ProgressBar = itemView.findViewById(R.id.progress_download)
    private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    private val btnAction: Button = itemView.findViewById(R.id.btn_action)
    private val btnDelete: TextView = itemView.findViewById(R.id.btn_delete)

    private val mainHandler = Handler(Looper.getMainLooper())
    private val executor = Executors.newFixedThreadPool(4)

    fun bind(
        app: AppInfo,
        status: DownloadStatus,
        progress: Int,
        onActionClick: (String) -> Unit
    ) {
        tvAppName.text = app.name
        tvPackageName.text = app.packageName
        tvDownloadCount.text = formatDownloadCount(app.downloadCount)

        // 加载应用图标
        loadAppIcon(app.icon)

        // 根据下载状态更新 UI
        when (status) {
            DownloadStatus.NOT_STARTED -> {
                layoutDownloadInfo.visibility = View.GONE
                btnAction.text = "下载"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.GONE
            }
            DownloadStatus.DOWNLOADING -> {
                layoutDownloadInfo.visibility = View.VISIBLE
                progressDownload.progress = progress
                tvStatus.text = "下载中 $progress%"
                btnAction.text = "暂停"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE
            }
            DownloadStatus.PAUSED -> {
                layoutDownloadInfo.visibility = View.VISIBLE
                progressDownload.progress = progress
                tvStatus.text = "已暂停 $progress%"
                btnAction.text = "继续"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE
            }
            DownloadStatus.SUCCESS -> {
                layoutDownloadInfo.visibility = View.VISIBLE
                progressDownload.progress = 100
                tvStatus.text = "下载完成"
                btnAction.text = "安装"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE
            }
            DownloadStatus.FAILED -> {
                layoutDownloadInfo.visibility = View.VISIBLE
                progressDownload.progress = progress
                tvStatus.text = "下载失败"
                btnAction.text = "重试"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE
            }
            DownloadStatus.INSTALLING -> {
                layoutDownloadInfo.visibility = View.VISIBLE
                tvStatus.text = "安装中..."
                btnAction.visibility = View.GONE
                btnDelete.visibility = View.GONE
            }
            DownloadStatus.INSTALLED -> {
                layoutDownloadInfo.visibility = View.GONE
                btnAction.text = "打开"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.GONE
            }
            DownloadStatus.DELETED -> {
                layoutDownloadInfo.visibility = View.GONE
                btnAction.text = "下载"
                btnAction.visibility = View.VISIBLE
                btnDelete.visibility = View.GONE
            }

            DownloadStatus.NOT_EXIST -> TODO()
        }

        btnAction.setOnClickListener {
            val action = when (status) {
                DownloadStatus.NOT_STARTED, DownloadStatus.DELETED -> "download"
                DownloadStatus.PAUSED, DownloadStatus.FAILED -> "resume"
                DownloadStatus.SUCCESS -> "install"
                DownloadStatus.INSTALLED -> "open"
                DownloadStatus.DOWNLOADING -> "pause"
                else -> ""
            }
            if (action.isNotEmpty()) onActionClick(action)
        }

        btnDelete.setOnClickListener {
            onActionClick("delete")
        }

        // 设置整个 item 的点击事件
        itemView.setOnClickListener {
            onActionClick("click")
        }
    }

    private fun formatDownloadCount(count: Int): String {
        return when {
            count >= 100000000 -> "${count / 100000000}亿下载"
            count >= 10000 -> "${count / 10000}万下载"
            else -> "${count}下载"
        }
    }

    private fun loadAppIcon(iconUrl: String?) {
        if (iconUrl.isNullOrEmpty()) {
            return
        }

        executor.execute {
            try {
                val url = URL(iconUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()

                    mainHandler.post {
                        if (bitmap != null) {
                            ivAppIcon.setImageBitmap(bitmap)
                        }
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                Log.e("AppViewHolder", "加载图标失败: ${e.message}", e)
            }
        }
    }
}
