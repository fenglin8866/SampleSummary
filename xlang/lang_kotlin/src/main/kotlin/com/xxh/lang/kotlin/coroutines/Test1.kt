package com.xxh.lang.kotlin.coroutines

import android.util.Log
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

object Test1 {
    fun test(){
        val c= suspend {
            Log.v("xxh","协程体")
            5
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                Log.v("xxh","协程 end result=${result.getOrNull()}")
            }

        })
        c.resume(Unit)
    }
}