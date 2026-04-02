package com.xxh.summary.net.model

import androidx.annotation.Keep

@Keep
open class BaseResponse<T>(
    val code: Int,
    val message: Any?,
    val data: T?
)