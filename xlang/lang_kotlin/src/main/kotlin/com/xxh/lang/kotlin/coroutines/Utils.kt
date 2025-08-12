package com.xxh.lang.kotlin.coroutines

fun log(tag: String = "xxh", msg: String) {
    println("[$tag][${Thread.currentThread().name}]:$msg")
}

fun intervalTime(msg: String = "", startTime: Long) {
    log(msg = "$msg  time=${System.currentTimeMillis() - startTime}")
}
