package com.sample.core.basic.test

data class OrderOptions(
    val extraSpicy: Boolean = false,
    val noOnion: Boolean = false
)
sealed class MainUIIntent {
    object LoadUser : MainUIIntent()
    object Logout : MainUIIntent()
    data class SubmitOrder(
        val id: String,
        val name: String,
        val count: Int,
        val options: OrderOptions
    ) : MainUIIntent()
    data class UpdateUsername(val username: String) : MainUIIntent()
}