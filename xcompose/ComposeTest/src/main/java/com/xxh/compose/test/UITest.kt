package com.xxh.compose.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xxh.compose.R

@Composable
fun UITestScreen() {
    val isVisible by remember { mutableStateOf(true) }
    if (!isVisible) {
        Card(
            modifier = Modifier.size(200.dp)
        ) {
            Column(
                modifier = Modifier
                    .size(200.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                `Icon` 和 `Image` 在 Compose 中有不同的用途和特性：
                主要区别
                1. **用途不同**
                - `Icon` 主要用于显示矢量图标和小图标
                - `Image` 用于显示各种类型的图片（包括位图、矢量图等）
                2. **颜色处理差异**
                - `Icon` 默认会应用 `tint` 颜色，默认是当前主题的主要颜色
                - `Image` 直接显示原始图片内容，不会自动着色
                3. **显示机制**
                - `Icon` 更适合单色图标，会忽略原图的颜色信息
                - `Image` 完整渲染图片的所有颜色信息
                ## 解决方案
                如果你想让 `Icon` 正常显示彩色图片，可以：
                tint = androidx.compose.ui.graphics.Color.Unspecified // 禁用默认着色
                或者直接使用 `Image` 组件来显示位图资源，这是更合适的选择。
                 */
                Icon(
                    painter = painterResource(id = R.drawable.ali),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    tint = Color.Unspecified
                )
                Image(
                    painter = painterResource(id = R.drawable.ali),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )
            }
        }
    }else{
        /*Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.ali), contentDescription = null,
                modifier = Modifier.size(90.dp)
                )
            Divider(Modifier.padding(20.dp))
            Card(shape = MaterialTheme.shapes.medium) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }*/
        // 在 Compose 中
        Column() {
            Box(
                modifier = Modifier
                    .size(200.dp, 52.dp)
                    .padding(10.dp)
                    .shadow(elevation = 13.dp)
                    .background(Color.White, RoundedCornerShape(26.dp))
            )
            Card(
                shape = RoundedCornerShape(26.dp),
                elevation = 8.dp,
                backgroundColor = Color.White,
                modifier = Modifier
                    .padding(20.dp)
                    .size(300.dp, 52.dp),
                border = CardDefaults.outlinedCardBorder(),
            ) {
                Text("搜索框", modifier = Modifier.padding(10.dp))
            }
        }

    }
}

@Preview(name = "Light Mode")
@Composable
fun UITestPreview() {
     UITestScreen()
}


