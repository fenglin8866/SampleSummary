package com.sample.lib.test.repository

import com.sample.lib.network.core.NetworkManager
import com.sample.lib.network.dispatcher.RequestKey
import com.sample.lib.network.dispatcher.RequestStrategy
import com.sample.lib.network.repository.RequestDispatcherBaseRepository
import com.sample.lib.test.api.UserApi
import kotlinx.coroutines.CoroutineScope
import java.util.Objects


class UserRepository2(val viewModelScope: CoroutineScope) : RequestDispatcherBaseRepository() {

    private val api = NetworkManager.create(UserApi::class.java)

    /**
     *防重复点击（登录按钮）
     */
    fun login(username: String, password: String) =
        request(
            scope = viewModelScope,
            key = RequestKey("login", Objects.hash(username, password)),
            strategy = RequestStrategy.DEBOUNCE
        ) {
            api.getUser(LoginRequest(username, password))
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

