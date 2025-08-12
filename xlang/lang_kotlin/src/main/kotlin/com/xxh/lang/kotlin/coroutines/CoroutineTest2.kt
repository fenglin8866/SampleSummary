package com.xxh.lang.kotlin.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

object CoroutineTest2 {
    private fun simple()= flow<Int> {
        repeat(3) {
            delay(1000)
            emit(it)
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun test1()= runBlocking {
        launch(newSingleThreadContext("singe")) {
            simple().collect {
                log(msg = "$it")
                log(msg = "collect")
            }
        }
    }

    /**
     * conflate
     */
    fun test2()= runBlocking {
        val t=measureTimeMillis {
            simple().conflate().collect {
                delay(3000)
                log(msg = "$it")
            }
        }
        log(msg = "$t")
    }

}
