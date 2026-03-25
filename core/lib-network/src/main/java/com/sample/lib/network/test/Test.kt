package com.sample.lib.network.test

import com.sample.lib.network.core.NetworkManager
import com.sample.lib.network.dispatcher.RequestKey
import com.sample.lib.network.dispatcher.RequestStrategy
import com.sample.lib.network.ext.onLoading
import com.sample.lib.network.ext.onSuccess
import com.sample.lib.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class Test {

    //动态切换 BaseUrl（全局）
    private fun switchBaseUrl() {
        NetworkManager.switchBaseUrl("https://api.test.com/")
    }
}





