package com.xxh.summary.net.env

object HostManager {

    private val hostMap = mapOf(
        Env.DEV to mapOf(
            HostType.CMS to "https://cms-test.nubia.com/api/",
            HostType.BROWSER to "https://browser-test.nubia.com/api/",
            HostType.ABROAD_CMS to "https://dev-abroad-cms.xxx.com/",
            HostType.ABROAD_BROWSER to "https://dev-abroad-browser.xxx.com/"
        ),
        Env.TEST to mapOf(
            HostType.CMS to "https://cms-test.nubia.com/api/",
            HostType.BROWSER to "https://browser-test.nubia.com/api/",
            HostType.ABROAD_CMS to "https://test-abroad-cms.xxx.com/",
            HostType.ABROAD_BROWSER to "https://test-abroad-browser.xxx.com/"
        ),
        Env.PROD to mapOf(
            HostType.CMS to "https://cms.redmagic.com/api/",
            HostType.BROWSER to "https://browser.redmagic.com/api/",
            HostType.ABROAD_CMS to "https://abroad-cms.xxx.com/",
            HostType.ABROAD_BROWSER to "https://abroad-browser.xxx.com/"
        )
    )

    fun getBaseUrl(hostType: String): String {
        val env = EnvManager.getEnv()
        return hostMap[env]?.get(hostType)
            ?: error("Host not found: $hostType in $env")
    }
}