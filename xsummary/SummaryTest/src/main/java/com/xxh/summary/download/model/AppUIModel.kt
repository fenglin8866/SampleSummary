package com.xxh.summary.download.model

data class AppUIModel(
    val appId: String,
    val name: String,
    val icon: String,
    val packageName: String,
    val appSize: String,
    val summary: String,
    val downloadUrl:String,
    val detailUrl:String,
    val progress: Int = 0,
    val status: Status = Status.IDLE,
)