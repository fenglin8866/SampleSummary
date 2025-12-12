package com.xxh.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xxh.compose.base.ConstraintLayoutSample
import com.xxh.compose.base.Message
import com.xxh.compose.base.MessageCard
import com.xxh.compose.base.MessageSample
import com.xxh.compose.effect.RememberCoroutineScopSample
import com.xxh.compose.effect.RememberUpdateStateSample
import com.xxh.compose.nav3.NavExample2
import com.xxh.compose.nav3.NavExample4
import com.xxh.compose.test.UITestScreen
import com.xxh.compose.ui.theme.SampleSummaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleSummaryTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                }*/
                //想要全局控制
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Test()
//                        UITestScreen()
                        //RememberUpdateStateSample()
                    }
                }
                //想要单独某个布局避开系统栏
                /*Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.statusBars.asPaddingValues())
                ) {

                }*/
            }
        }
    }
}




@Composable
fun Test() {
    //RememberCoroutineScopSample()
    //RememberUpdateStateSample()
    //MessageSample()
//    ConstraintLayoutSample()
    NavExample4()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleSummaryTheme {
        Test()
    }
}