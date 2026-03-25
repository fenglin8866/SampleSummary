package com.sample.lib.network.dispatcher

enum class RequestStrategy {
    /** 默认：每次都请求 */
    NORMAL,

    /** 防重复点击（短时间内只执行一次） */
    DEBOUNCE,

    /** 相同请求只执行一次（合并） */
    MERGE,

    /** 只保留最后一次（前面的取消） */
    CANCEL_PREVIOUS
}