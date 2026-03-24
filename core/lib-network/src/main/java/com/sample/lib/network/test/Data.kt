package com.sample.lib.network.test

data class LoginRequest(
    val username: String,
    val password: String
)

data class User(
    val id: Int,
    val name: String
)