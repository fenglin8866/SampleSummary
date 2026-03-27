package com.xxh.summary.download.ui

import com.xxh.summary.download.data.model.AppInfo
import com.xxh.summary.download.data.model.DownloadState

data class AppListState(
    val list: List<AppInfo> = emptyList(),
    val downloadMap: Map<String, DownloadState> = emptyMap()
)