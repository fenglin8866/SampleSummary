package com.xxh.summary.download.model

data class AppListState(
    val list: List<AppInfo> = emptyList(),
    val downloadMap: Map<String, DownloadState> = emptyMap()
)