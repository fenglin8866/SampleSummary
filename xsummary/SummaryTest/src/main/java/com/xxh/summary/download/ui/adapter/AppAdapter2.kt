package com.xxh.summary.download.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.nubia.ns_lib.download.DownloadStatus
import com.xxh.summary.R
import com.xxh.summary.download.model.AppInfo

class AppAdapter2(
    private var appList: List<AppInfo>,
    private val onDownloadClick: (AppInfo) -> Unit,
    /* private val onDownloadClick: (NeoAppInfo) -> Unit,
     private val onPauseClick: (NeoAppInfo) -> Unit,
     private val onResumeClick: (NeoAppInfo) -> Unit,
     private val onDeleteClick: (NeoAppInfo) -> Unit,
     private val onItemClick: (NeoAppInfo) -> Unit*/
) : RecyclerView.Adapter<AppViewHolder>() {

    private val statusMap = mutableMapOf<String, Pair<DownloadStatus, Int>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = appList[position]
        val (status, progress) = statusMap[app.packageName] ?: (DownloadStatus.NOT_STARTED to 0)

        holder.bind(app, status, progress) { action ->
            when (action) {
                "download" -> onDownloadClick(app)
              /*  "download" -> onDownloadClick(app)
                "pause" -> onPauseClick(app)
                "resume" -> onResumeClick(app)
                "delete" -> onDeleteClick(app)
                "click" -> onItemClick(app)*/
            }
        }
    }

    override fun getItemCount(): Int = appList.size

    override fun onViewAttachedToWindow(holder: AppViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            // 曝光埋点
        }
    }

    fun updateStatus(packageName: String, status: DownloadStatus, progress: Int) {
        statusMap[packageName] = status to progress
        notifyItemChanged(appList.indexOfFirst { it.packageName == packageName })
    }

    fun updateAppList(newList: List<AppInfo>) {
        appList = newList
        statusMap.clear()
        notifyDataSetChanged()
    }
}
