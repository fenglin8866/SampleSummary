package com.sample.lib.network.auth

/**
 * Token 管理（支持自动刷新）
 */
object TokenManager {

    @Volatile
    var token: String? = null

    @Volatile
    var refreshToken: String? = null

    fun update(newToken: String, newRefreshToken: String) {
        token = newToken
        refreshToken = newRefreshToken
    }

    fun clear() {
        token = null
        refreshToken = null
    }
}