package com.xxh.summary.download.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AppListResponse(
    @SerializedName("dataList")
    val dataList: List<AppInfo>
)

@Keep
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