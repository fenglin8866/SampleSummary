package com.xxh.summary.download.model

import com.google.gson.annotations.SerializedName

data class AppRankListResponse(
    @SerializedName("dataList")
    val dataList: List<AppInfo>
)

data class AppInfo(
    @SerializedName("softId")
    val appId: String,
    @SerializedName("softName")
    val name: String,
    @SerializedName("iconUrl")
    val icon: String,
    @SerializedName("downloadUrl")
    val downloadUrl: String,
    @SerializedName("pkgName")
    val packageName: String,
    @SerializedName("downloadCnt")
    val downloadCount: Int,
    @SerializedName("detailUrl")
    val detailUrl: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("fileSize")
    val fileSize: String,
    @SerializedName("itemName")
    val itemType: String
)