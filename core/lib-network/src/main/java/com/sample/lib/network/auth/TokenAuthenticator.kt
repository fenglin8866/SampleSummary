package com.sample.lib.network.auth

import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.Authenticator

/**
 * Token 自动刷新
 */
/*
class TokenAuthenticator(
    private val refreshApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        // 避免死循环
        if (responseCount(response) >= 2) return null

        val refreshToken = TokenManager.refreshToken ?: return null

        synchronized(this) {
            val newToken = runCatching {
                refreshApi.refreshToken(mapOf("refreshToken" to refreshToken))
            }.getOrNull()

            newToken?.data?.let {
                TokenManager.update(it.token, it.refreshToken)

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.token}")
                    .build()
            }
        }

        return null
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}*/
