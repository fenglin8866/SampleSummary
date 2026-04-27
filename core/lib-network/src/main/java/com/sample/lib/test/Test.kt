package com.sample.lib.test

import com.sample.lib.network.core.NetworkManager

class Test {

    //动态切换 BaseUrl（全局）
    private fun switchBaseUrl() {
        NetworkManager.switchBaseUrl("https://api.test.com/")
    }
}





