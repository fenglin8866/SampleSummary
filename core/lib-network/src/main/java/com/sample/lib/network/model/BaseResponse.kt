package com.sample.lib.network.model

import androidx.annotation.Keep

@Keep
data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)