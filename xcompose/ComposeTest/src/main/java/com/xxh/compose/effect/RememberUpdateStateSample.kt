package com.xxh.compose.effect

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.delay
/**
 *  界面的重组与State紧密关联
 *  注意每个可组合函数的作用域，特别非状态的逻辑
 *  rememberUpdatedState本质是 remember { mutableStateOf(newValue) }
 *  注意LandingScreen3与LandingScreen5的区别
 */
@Composable
fun RememberUpdateStateSample() {
    //LandingScreen5()
    LandingScreen3()
}

@Composable
fun LandingScreen() {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val timeout1:() -> Unit = {
        Log.d("xxh", "最后执行方法1")
    }
    val timeout2:() -> Unit = {
        Log.d("xxh", "最后执行方法2")
    }
    val state = remember {
        mutableStateOf(timeout1)
    }

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        state.value.invoke()
    }

    Column {
        Button(onClick = {
            state.value = timeout2
        }) {
            Text("切换最后执行方法")
        }
    }
}


@Composable
fun LandingScreen2() {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val timeout1:() -> Unit = {
        Log.d("xxh", "最后执行方法1")
    }
    val timeout2:() -> Unit = {
        Log.d("xxh", "最后执行方法2")
    }
    val state = remember {
        mutableStateOf(timeout1)
    }
    val currentOnTimeout by rememberUpdatedState(state.value)

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        currentOnTimeout()
    }

    Column {
        Button(onClick = {
            state.value = timeout2
        }) {
            Text("切换最后执行方法")
        }
    }
}


@Composable
fun LandingScreen3() {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    var timeout:() -> Unit = {
        Log.d("xxh", "最后执行方法1")
    }

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        timeout()
    }

    Column {
        Button(onClick = {
            timeout = {
                Log.d("xxh", "最后执行方法2")
            }
        }) {
            Text("切换最后执行方法")
        }
    }
}

@Composable
fun LandingScreen4() {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val timeout1:() -> Unit = {
        Log.d("xxh", "最后执行方法1")
    }
    val timeout2:() -> Unit = {
        Log.d("xxh", "最后执行方法2")
    }
    val state = remember {
        mutableStateOf(timeout1)
    }
    val currentOnTimeout by rememberUpdatedState(state.value)

    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        currentOnTimeout()
    }

    Column {
        Button(onClick = {
            state.value = timeout2
        }) {
            Text("切换最后执行方法")
        }
    }
}

@Composable
fun LandingScreen5() {
    /*val timeout1:() -> Unit = {
        Log.d("xxh", "最后执行方法1")
    }
    val timeout2:() -> Unit = {
        Log.d("xxh", "最后执行方法2")
    }
    val state = remember {
        mutableStateOf(timeout1)
    }
    //LandingScreen(state.value)
    LandingScreen(state)
    Column {
        Button(onClick = {
            state.value = timeout2
        }) {
            Text("切换最后执行方法")
        }
    }*/
    var timeout:() -> Unit = {
        Log.d("xxh", "最后执行方法1")
    }

    LandingScreenA(timeout)

    Column {
        Button(onClick = {
            timeout = {
                Log.d("xxh", "最后执行方法2")
            }
        }) {
            Text("切换最后执行方法")
        }
    }
}

@Composable
fun LandingScreen(timeOut: () -> Unit) {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState(timeOut)
    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        currentOnTimeout()
       // timeOut()
    }
}


@Composable
fun LandingScreenA(timeOut: () -> Unit) {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    val currentOnTimeout by rememberUpdatedState(timeOut)
    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        timeOut()
    }
}


@Composable
fun LandingScreen(state: State<() -> Unit>) {
    // This will always refer to the latest onTimeout function that
    // LandingScreen was recomposed with
    //val currentOnTimeout by rememberUpdatedState(timeOut)
    // Create an effect that matches the lifecycle of LandingScreen.
    // If LandingScreen recomposes, the delay shouldn't start again.
    LaunchedEffect(true) {
        repeat(10) {
            delay(1000)
            Log.d("xxh", "执行了 $it 次")
        }
        state.value.invoke()
    }
}

