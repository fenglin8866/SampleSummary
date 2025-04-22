package com.sample.core.demo.intent

/**
 * 通用 UIIntent：
 * 支持携带多个参数
 */
sealed class UIIntent {
    data class Snackbar(val message: String) : UIIntent()
    data class Navigate(val route: String, val params: Map<String, Any?> = emptyMap()) : UIIntent()
    data class RequestPermission(val permissions: List<String>) : UIIntent()
    data class Custom(val tag: String, val extras: Map<String, Any?> = emptyMap()) : UIIntent()
}