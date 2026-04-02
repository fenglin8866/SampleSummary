package com.xxh.summary.net.dispatcher

/**
 * 请求去重
 * 请求合并
 */
data class RequestKey(
    val path: String,
    val paramsHash: Int,
    val tag: String? = null
)