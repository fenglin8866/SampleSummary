package com.xxh.compose.utils

import android.util.Log

object XLog {
    private const val TAG = "Compose"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

}