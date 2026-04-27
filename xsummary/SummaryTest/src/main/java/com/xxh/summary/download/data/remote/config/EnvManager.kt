package com.xxh.summary.download.data.remote.config

import com.sample.lib.network.core.NetworkManagerMultiBaseUrl

/**
 * 环境管理器
 * EnvManager.switch(Env.DEV)
 * 可以做一个调试面板
 */
object EnvManager {

    @Volatile
    private var currentEnv: Env = Env.PROD

    fun getEnv(): Env = currentEnv

    fun switch(env: Env) {
        currentEnv = env
        NetworkManagerMultiBaseUrl.onEnvChanged()
    }

    fun isTestEnv(): Boolean = currentEnv == Env.TEST

}