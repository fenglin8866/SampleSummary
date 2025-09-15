package com.xxh.compose.effect

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldSample(
    state: MutableState<Boolean>,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    LaunchedEffect(state.value) {
        scaffoldState.snackbarHostState.showSnackbar(
            "Error message","Retry message"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("脚手架示例") }
            )
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    state.value=!state.value
                }) {
                    Text("Error occurs")
                }
            }
        }
    )
}

@Composable
fun LaunchedEffectSample() {
    val state= remember{
        mutableStateOf(false)
    }
    ScaffoldSample(state)
}