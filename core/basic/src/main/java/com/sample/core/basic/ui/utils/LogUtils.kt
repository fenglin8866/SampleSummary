package com.sample.core.basic.ui.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object LogUtils {

    // 全局日志开关
    var isDebug = true

    // 默认TAG
    private const val DEFAULT_TAG = "LogUtils"

    // 每段最大字符数（防止超长日志被截断）
    private const val MAX_LOG_LENGTH = 3000

    private val timerMap = mutableMapOf<String, Long>()

    fun init(debug: Boolean) {
        isDebug = debug
    }

    private fun getCallerInfo(): String {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size < 4) return ""
        val element = stackTrace[3]
        return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
    }

    private fun log(tag: String, msg: String, level: Int, throwable: Throwable? = null) {
        if (!isDebug) return

        val content = "${getCallerInfo()} ➔ $msg"
        if (content.length <= MAX_LOG_LENGTH) {
            printLog(tag, content, level, throwable)
        } else {
            // 分段打印
            var index = 0
            val length = content.length
            while (index < length) {
                val sub = content.substring(index, (index + MAX_LOG_LENGTH).coerceAtMost(length))
                printLog(tag, sub, level, throwable)
                index += MAX_LOG_LENGTH
            }
        }
    }

    private fun printLog(tag: String, msg: String, level: Int, throwable: Throwable?) {
        when (level) {
            Log.VERBOSE -> Log.v(tag, msg)
            Log.DEBUG -> Log.d(tag, msg)
            Log.INFO -> Log.i(tag, msg)
            Log.WARN -> Log.w(tag, msg)
            Log.ERROR -> {
                if (throwable != null) {
                    Log.e(tag, msg, throwable)
                } else {
                    Log.e(tag, msg)
                }
            }
        }
    }

    fun v(tag: String = DEFAULT_TAG, message: String) = log(tag, message, Log.VERBOSE)
    fun d(tag: String = DEFAULT_TAG, message: String) = log(tag, message, Log.DEBUG)
    fun i(tag: String = DEFAULT_TAG, message: String) = log(tag, message, Log.INFO)
    fun w(tag: String = DEFAULT_TAG, message: String) = log(tag, message, Log.WARN)
    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) =
        log(tag, message, Log.ERROR, throwable)

    /**
     * 打印美化后的Json
     */
    fun json(tag: String = DEFAULT_TAG, json: String) {
        if (!isDebug) return
        try {
            val jsonFormatted = when {
                json.trim().startsWith("{") -> {
                    val jsonObject = JSONObject(json)
                    jsonObject.toString(4)
                }

                json.trim().startsWith("[") -> {
                    val jsonArray = JSONArray(json)
                    jsonArray.toString(4)
                }

                else -> {
                    json // 不是标准Json，原样输出
                }
            }
            d(tag, jsonFormatted)
        } catch (e: JSONException) {
            e(tag, "Invalid JSON format", e)
        }
    }

    /**
     * 开始记录耗时
     */
    fun startTimer(tag: String) {
        if (!isDebug) return
        timerMap[tag] = System.currentTimeMillis()
    }

    /**
     * 结束记录耗时并打印
     */
    fun endTimer(tag: String) {
        if (!isDebug) return
        val startTime = timerMap[tag]
        if (startTime != null) {
            val duration = System.currentTimeMillis() - startTime
            d(DEFAULT_TAG, "[$tag] 耗时：${duration}ms")
            timerMap.remove(tag) // 释放内存
        } else {
            w(DEFAULT_TAG, "[$tag] 未找到 startTimer，请确认调用顺序")
        }
    }

    /**
     * 开始一个日志块
     */
    fun startBlock(title: String) {
        if (!isDebug) return
        val blockTitle = "╔═══════════ 开始 $title ═══════════╗"
        d(DEFAULT_TAG, blockTitle)
    }

    /**
     * 结束一个日志块
     */
    fun endBlock(title: String) {
        if (!isDebug) return
        val blockTitle = "╚═══════════ 结束 $title ═══════════╝"
        d(DEFAULT_TAG, blockTitle)
    }
}
