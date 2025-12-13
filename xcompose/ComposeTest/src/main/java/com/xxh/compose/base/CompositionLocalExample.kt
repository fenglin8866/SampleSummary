package com.xxh.compose.base

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xxh.compose.utils.XLog

//显式传参
class ExplicitTest {
    private fun layout() {
        val color = "黑色"
        text(color)
        grid(color)
        text(color)
        text(color)
    }

    private fun grid(color: String) {
        XLog.d("other components in grid")
        text(color)
    }

    private fun text(color: String) {
        XLog.d("Text")
        XLog.d(color)
    }

    fun testExplicit() {
        layout()
    }
}

//隐式传参
class ImplicitTest {
    var color = "黑色"
    private fun layout() {
        text()
        // color = "红色"
        provider("红色") {
            grid()
        }
        text()
        text()
    }

    private fun grid() {
        XLog.d("other components in grid")
        text()
    }

    private fun text() {
        XLog.d("Text")
        XLog.d(color)
    }

    private fun provider(value: String, content: () -> Unit) {
        color = value
        content()
        color = "黑色"
    }

    fun testImplicit() {
        layout()
    }
}


@Composable
fun CompositionLocalExample() {
    MaterialTheme {
        // Surface provides contentColorFor(MaterialTheme.colorScheme.surface) by default
        // This is to automatically make text and other content contrast to the background
        // correctly.
        Surface {
            Column {
                Text("Uses Surface's provided content color")
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                    Text("Primary color provided by LocalContentColor")
                    Text("This Text also uses primary as textColor")
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
                        DescendantExample()
                    }
                }
                Text("This Text also uses primary as textColor")
            }
        }
    }
}

@Composable
fun DescendantExample() {
    // CompositionLocalProviders also work across composable functions
    Text("This Text uses the error color now")
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewSample() {
    CompositionLocalExample()
//    Content()
}


data class Elevations(val card: Dp = 0.dp, val default: Dp = 0.dp)


@Composable
fun Content() {
    Column(modifier = Modifier.padding(10.dp)) {
        val LocalElevations = compositionLocalOf { Elevations() }
        Text("Hi")
        CompositionLocalProvider(LocalElevations provides Elevations(5.dp)) {
            MyCard(LocalElevations.current.card)
        }
    }
    Box {

    }
}

@Composable
fun MyCard(level: Dp) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = level),
        modifier = Modifier.size(200.dp)
    ) {
        Text("This is a card")
    }
}


@Composable
fun CreateCompositionLocalSample() {
    var isStatic = false
    var compositionLocalName = ""
    val currentLocalColor = if (isStatic) {
        compositionLocalName = "StaticCompositionLocal场景"
        staticCompositionLocalOf { Color.Black }
    } else {
        compositionLocalName = "DynamicCompositionLocal场景"
        compositionLocalOf { Color.Black }
    }

    var recomposeTag = "Init"


    var color by remember { mutableStateOf(Color.Green) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = compositionLocalName)
            Spacer(Modifier.height(20.dp))
            CompositionLocalProvider(currentLocalColor provides color) {
                TaggedBox("Wrapper:$recomposeTag", 400.dp, Color.Red) {
                    TaggedBox("Middle:$recomposeTag", 300.dp, currentLocalColor.current) {
                        TaggedBox("Inner:$recomposeTag", 200.dp, Color.Yellow) {

                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                color = Color.Blue
                recomposeTag = "Recompose"
            }) {
                Text("change theme")
            }
        }
    }
}

@Composable
fun TaggedBox(tag: String, size: Dp, background: Color, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .size(size)
            .background(background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(tag)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            content()
        }
    }
}





