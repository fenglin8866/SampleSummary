package com.sample.lib.network.model

class ApiException(
    val code: Int,
    override val message: String
) : Exception()