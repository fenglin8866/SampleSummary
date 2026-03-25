package com.sample.lib.network.model

import androidx.annotation.Keep

@Keep
open class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)