package com.xxh.summary.download.model

data class AppUIModel(
    val appId: String,
    val name: String,
    val icon: String,
    val packageName: String,
    val appSize:String,
    val summary: String,
    val progress: Int,
    val status: Status
)