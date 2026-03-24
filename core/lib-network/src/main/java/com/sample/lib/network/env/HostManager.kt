package com.sample.lib.network.env

object HostManager {

    private val hostMap = mapOf(
        Env.DEV to mapOf(
            HostType.USER to "https://dev-user.xxx.com/",
            HostType.ORDER to "https://dev-order.xxx.com/",
            HostType.UPLOAD to "https://dev-upload.xxx.com/"
        ),
        Env.TEST to mapOf(
            HostType.USER to "https://test-user.xxx.com/",
            HostType.ORDER to "https://test-order.xxx.com/",
            HostType.UPLOAD to "https://test-upload.xxx.com/"
        ),
        Env.PROD to mapOf(
            HostType.USER to "https://user.xxx.com/",
            HostType.ORDER to "https://order.xxx.com/",
            HostType.UPLOAD to "https://upload.xxx.com/"
        )
    )

    fun getBaseUrl(hostType: String): String {
        val env = EnvManager.getEnv()
        return hostMap[env]?.get(hostType)
            ?: error("Host not found: $hostType in $env")
    }
}