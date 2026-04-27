package com.sample.lib.test.repository

import com.sample.lib.network.core.NetworkManager
import com.sample.lib.network.core.NetworkManagerMultiBaseUrl
import com.sample.lib.network.repository.BaseRepository
import com.sample.lib.test.api.UserApi
import com.sample.lib.test.env.HostManager
import com.sample.lib.test.env.HostType

/**
 * viewModelScope.launch {
 *     repository.xxx()
 *         .onLoading { showLoading() }
 *         .onSuccess { render(it) }
 *         .onError { error, code ->
 *
 *             if (code == 401) {
 *                 goLogin()
 *             } else {
 *                 showError(error.message)
 *             }
 *
 *         }
 *         .collect()
 * }
 */
class UserRepository : BaseRepository() {

    private val api = NetworkManager.create(UserApi::class.java)

   /* fun login(username: String, password: String) =
        request {
            api.login(LoginRequest(username, password))
        }*/

    private val api2 = NetworkManagerMultiBaseUrl.create(HostManager.getAppBaseUrl(), UserApi::class.java)



    fun getUser(username: String, password: String) =
        request {
            api2.getUser(LoginRequest(username, password))
        }
}

