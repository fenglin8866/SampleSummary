package com.example.summary

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.lib.LibMainActivity
import com.example.summary.sample.ui.book.BookActivity
import com.example.summary.sample.ui.user.UserActivity
import com.example.summary.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current // 获取 Context
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(onClick = {
            // 创建 Intent 跳转到目标 Activity
            val intent = Intent(context, LibMainActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "跳转lib模块",
                modifier = modifier
            )
        }
        Button(onClick = {
            val intent = Intent(context, BookActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "跳转book界面",
                modifier = modifier
            )
        }
        Button(onClick = {
            val intent = Intent(context, UserActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "跳转user界面",
                modifier = modifier
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}