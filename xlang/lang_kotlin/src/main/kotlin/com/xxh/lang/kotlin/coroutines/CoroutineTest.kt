package com.xxh.lang.kotlin.coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

object CoroutineTest {
    /**
     * 协程的创建
     */
    private var startTime = System.currentTimeMillis();
    fun test1() = runBlocking<Unit> {
        launch {
            delay(3000)
            intervalTime("launch", startTime)
        }

        GlobalScope.launch {
            delay(1500)
            intervalTime(" GlobalScope.launch ", startTime)
        }

        withContext(Dispatchers.IO) {
            delay(1100)
            intervalTime("withContext", startTime)
        }

        coroutineScope {
            delay(2000)
            intervalTime("coroutineScope", startTime)
        }

        async {
            delay(100)
            intervalTime("async", startTime)
        }

        withTimeoutOrNull(4500) {
            delay(4000)
            intervalTime("withTimeoutOrNull", startTime)
        }

        /*
           [DefaultDispatcher-worker-2 @coroutine#1] withContext  time=1132
           [DefaultDispatcher-worker-2 @coroutine#3]  GlobalScope.launch   time=1521
           [main @coroutine#2] launch  time=3030
           [main @coroutine#1] coroutineScope  time=3141
           [main @coroutine#4] async  time=3248
           [main @coroutine#1] withTimeoutOrNull  time=7151
         */
    }

    /**
     * 结构化并发
     */
    fun test2() = runBlocking {
        val job1 = launch {
            launch {
                /*while (System.currentTimeMillis() - stTime > 100)
                    println("子协程")*/
                repeat(5) {
                    delay(100)
                    println("$it 子协程1")
                }
            }
            launch {
                repeat(5) {
                    delay(90)
                    println("$it 子协程2")
                    if (it == 3) {
//                        throw CancellationException()
                        throw Exception()
                    }
                }
            }
            repeat(5) {
                delay(98)
                println("$it 父协程")
            }

        }
        //delay(370)
        // job1.cancel()
        //job1.cancelChildren()
    }

    /**
     * 超时
     */
    fun test3() = runBlocking {
        try {
            withTimeout(300) {
                delay(400)
            }
        } catch (e: TimeoutCancellationException) {
            println("withTimeout")
        }

        withTimeoutOrNull(300) {
            delay(400)
        }
    }

    /**
     * async的使用
     */
    fun test4() = runBlocking {
        val a1 = async {
            delay(300)
            1
        }
        val a2 = async {
            delay(500)
            //return@async 4
            4
        }
        launch {
            intervalTime(startTime = startTime)
            a1.await() + a2.await()
            intervalTime(startTime = startTime)
        }
    }

    /**
     * coroutineScope测试
     */
    fun test5() = runBlocking {
        launch {
            delay(1000)
            intervalTime("launch1", startTime)
        }
        coroutineScope {
            launch {
                delay(3000)
                intervalTime("coroutineScope launch1", startTime)
            }
            delay(2000)
            intervalTime("coroutineScope", startTime)
        }
        launch {
            delay(1000)
            intervalTime("launch2", startTime)
        }

    }

    /**
     * coroutineScope子协程中运行
     */
    fun test6() = runBlocking {
        launch {
            delay(1000)
            intervalTime("test6 launch1", startTime)
            coroutineScope {
                delay(2000)
            }
            intervalTime("test6 launch1-2", startTime)
        }
        launch {
            delay(1000)
            intervalTime("test6 launch2", startTime)
        }
    }

    /**
     * 对比async中使用方法参数与coroutineScope的区别
     * 1.阻塞效果是相同的
     * 2.只有并发，效果不一致，coroutineScope会阻塞
     */
    fun test7() = runBlocking {
        launch {
            delay(1000)
            intervalTime("test7 launch1", startTime)
        }
//        concurrentSum()
        testAsync(this)
        launch {
            delay(1000)
            intervalTime("test7 launch2", startTime)
        }
    }

    /**
     * 调度器
     */
    fun test8() = runBlocking {
        val job1 = launch {
            delay(2000)
//            delay(400)
            log(msg = "main")
        }
        val job2 = launch(Dispatchers.Default) {
            log(msg = "Default")
            delay(1000)
            log(msg = "Default")
        }
        withContext(Dispatchers.Unconfined) {
            log(msg = "withContext0")
            job2.join()
            log(msg = "withContext1")
            job1.join()
            // delay(1000)
            log(msg = "withContext2")
        }
    }

    /*
    [xxh][DefaultDispatcher-worker-1 @coroutine#3]:Default
    [xxh][main @coroutine#1]:withContext0
    [xxh][DefaultDispatcher-worker-1 @coroutine#3]:Default
    [xxh][DefaultDispatcher-worker-1 @coroutine#1]:withContext1
    [xxh][main @coroutine#2]:main
    [xxh][main @coroutine#1]:withContext2
     */

    private suspend fun concurrentSum() = coroutineScope {
        val one = async<Int> {
            try {
                delay(1000) // 模拟一个长时间的运算
                42
            } finally {
            }
        }
        val two = async<Int> {
            delay(1000)
            48
        }
        val sum = one.await() + two.await()
        intervalTime("concurrentSum = $sum", startTime)
    }

    /**
     * async结构化并发
     */
    suspend fun failedConcurrentSum() = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }

}

suspend fun testLaunch(scope: CoroutineScope) {
    val time = measureTimeMillis {
        val j = scope.launch {
            delay(1000)
        }
        val j2 = scope.launch {
            delay(3000)
            // println("Completed in $time2 ms")
        }

    }

}


//测试协程async相关
suspend fun testAsync(scope: CoroutineScope) {
    val time = measureTimeMillis {
        val one = scope.async { doSomethingUsefulOne() }
        val two = scope.async { doSomethingUsefulTwo() }
        /* val one = scope.async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
         val two = scope.async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
         one.start()
         two.start()*/
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了些有用的事
    return 29
}