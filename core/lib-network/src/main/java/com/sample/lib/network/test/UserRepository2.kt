package com.sample.lib.network.test

import com.sample.lib.network.core.NetworkManager
import com.sample.lib.network.dispatcher.RequestKey
import com.sample.lib.network.dispatcher.RequestStrategy
import com.sample.lib.network.repository.BaseRepositoryWithRequestDispatcher
import kotlinx.coroutines.CoroutineScope


class UserRepository2(val viewModelScope: CoroutineScope) : BaseRepositoryWithRequestDispatcher() {

    private val api = NetworkManager.create(ApiService::class.java)

    /**
     *防重复点击（登录按钮）
     */
    fun login(username: String, password: String) =
        request(
            scope = viewModelScope,
            key = RequestKey("login", username.hashCode()),
            strategy = RequestStrategy.DEBOUNCE
        ) {
            api.login(LoginRequest(username, password))
        }

    /**
     * 请求去重（列表加载）
     */
    fun loadList() =
        request(
            scope = viewModelScope,
            key = RequestKey("list", 0),
            strategy = RequestStrategy.MERGE
        ) {
            api.getList()
        }

    /**
     * 只保留最后一次（搜索）
     */
    fun search(keyword: String) =
        request(
            scope = viewModelScope,
            key = RequestKey("search", 0),
            strategy = RequestStrategy.CANCEL_PREVIOUS
        ) {
            api.search(keyword)
        }
}

