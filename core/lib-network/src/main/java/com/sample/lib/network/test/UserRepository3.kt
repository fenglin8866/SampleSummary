package com.sample.lib.network.test

import com.sample.lib.network.core.NetworkManager
import com.sample.lib.network.core.NetworkManagerMultiBaseUrl
import com.sample.lib.network.dispatcher.RequestDispatcher
import com.sample.lib.network.dispatcher.RequestKey
import com.sample.lib.network.dispatcher.RequestStrategy
import com.sample.lib.network.env.HostType
import com.sample.lib.network.model.ResultState
import com.sample.lib.network.repository.BaseRepository
import com.sample.lib.network.repository.BaseRepositoryWithRequestDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


class UserRepository3 : BaseRepositoryWithRequestDispatcher() {

    // ===== API =====
    private val api = NetworkManagerMultiBaseUrl.create(
        HostType.USER,
        UserApi2::class.java
    )

    // ===== 获取用户信息 =====
    fun getUser(
        scope: CoroutineScope,
        tag: String = "UserRepository"
    ): Flow<ResultState<User>> {

        return request(
            scope = scope,
            key = RequestKey(
                path = "getUser",
                paramsHash = 0,
                tag = tag
            ),
            strategy = RequestStrategy.MERGE // 防重复请求
        ) {
            api.getUser()
        }
    }

    // ===== 登录（防重复点击）=====
    fun login(
        scope: CoroutineScope,
        username: String,
        password: String,
        tag: String = "UserRepository"
    ): Flow<ResultState<User>> {

        return request(
            scope = scope,
            key = RequestKey(
                path = "login",
                paramsHash = (username + password).hashCode(),
                tag = tag
            ),
            strategy = RequestStrategy.DEBOUNCE // 防抖
        ) {
            api.login(
                LoginRequest(username, password)
            )
        }
    }

    // ===== 搜索（只保留最后一次）=====
    fun search(
        scope: CoroutineScope,
        keyword: String,
        tag: String = "UserRepository"
    ): Flow<ResultState<List<User>>> {

        return request(
            scope = scope,
            key = RequestKey(
                path = "search",
                paramsHash = 0,
                tag = tag
            ),
            strategy = RequestStrategy.CANCEL_PREVIOUS // 取消旧请求
        ) {
            api.search(keyword)
        }
    }

    // ===== 登出（示例普通请求）=====
    fun logout(
        scope: CoroutineScope,
        tag: String = "UserRepository"
    ): Flow<ResultState<Unit>> {

        return request(
            scope = scope,
            key = RequestKey(
                path = "logout",
                paramsHash = 0,
                tag = tag
            ),
            strategy = RequestStrategy.NORMAL
        ) {
            api.logout()
        }
    }

    // ===== 页面销毁取消请求 =====
    fun cancelRequests(tag: String = "UserRepository") {
        RequestDispatcher.cancelByTag(tag)
    }
}

