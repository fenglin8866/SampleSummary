package com.xxh.compose.base

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.hardware.lights.Light
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
  //  CompositionLocalExample()
    Content()
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






