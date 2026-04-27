package com.xxh.summary.download.data.remote.config

object ConfigManager {

    private val configMap = mapOf(
        Env.TEST to mapOf(
            ConfigType.APP_ID to "1310140",
            ConfigType.SECRET_KEY to "a680e0f41d5e4d63922d398d58a52c9a",
        ),
        Env.PROD to mapOf(
            ConfigType.APP_ID to "353402",
            ConfigType.SECRET_KEY to "583193d7b808483eb8fd02535758bde1",
        )
    )

    fun getAppId(): String {
        val env = EnvManager.getEnv()
        return configMap[env]?.get(ConfigType.APP_ID)
            ?: error("Host not found: ${ConfigType.APP_ID} in $env")
    }

    fun getSecretKey(): String {
        val env = EnvManager.getEnv()
        return configMap[env]?.get(ConfigType.SECRET_KEY)
            ?: error("Host not found: ${ConfigType.SECRET_KEY} in $env")
    }
}