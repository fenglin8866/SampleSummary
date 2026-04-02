package com.xxh.summary.net.model

class ApiException(
    val code: Int,
    override val message: String
) : Exception()