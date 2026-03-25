package com.sample.lib.network.dispatcher

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * 按页面取消
 * override fun onCleared() {
 *     RequestDispatcher.cancelByTag("UserPage")
 * }
 *
 * 按页面取消大部分情况下不需要，依赖ViewModelScope,根据ViewModel的生命周期来管理请求
 */
object RequestDispatcher {

    /** 正在执行的请求 */
    private val runningJobs = mutableMapOf<RequestKey, Job>()

    /** 防抖时间记录 */
    private val debounceMap = mutableMapOf<RequestKey, Long>()

    private const val DEBOUNCE_TIME = 500L

    fun <T> execute(
        scope: CoroutineScope,
        key: RequestKey,
        strategy: RequestStrategy = RequestStrategy.NORMAL,
        block: suspend () -> T
    ): Flow<T> = flow {

        when (strategy) {

            RequestStrategy.DEBOUNCE -> {
                val now = System.currentTimeMillis()
                val lastTime = debounceMap[key] ?: 0
                if (now - lastTime < DEBOUNCE_TIME) return@flow
                debounceMap[key] = now
            }

            RequestStrategy.MERGE -> {
                if (runningJobs.containsKey(key)) {
                    // 已有请求在执行，直接返回
                    return@flow
                }
            }

            RequestStrategy.CANCEL_PREVIOUS -> {
                runningJobs[key]?.cancel()
            }

            else -> {}
        }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            try {
                emit(block())
            } finally {
                runningJobs.remove(key)
            }
        }

        runningJobs[key] = job
        job.start()

        job.join()

    }

    fun cancelByTag(tag: String) {
        runningJobs
            .filter { it.key.tag == tag }
            .forEach { it.value.cancel() }
    }

}