package com.xxh.compose.base

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ConstraintLayoutSample() {
}


@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        //通过createRefs创建引用，ConstraintLayout中的每一个元素都需要关联一个引用
        val (button, text) = createRefs()

        Button(onClick = {}, modifier = Modifier.constrainAs(button) {
            top.linkTo(parent.top, margin = 16.dp)
        }) {
            Text(text = "Button")
        }
        Text(text = "Text", modifier = Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 16.dp)
            //ConstraintLayout中水平居中
            centerHorizontallyTo(parent)
        })
    }
}

@Composable
fun ConstraintLayoutContent2() {
    ConstraintLayout {
        //通过createRefs创建引用，ConstraintLayout中的每一个元素都需要关联一个引用
        val (button1,button2, text) = createRefs()

        Button(onClick = {}, modifier = Modifier.constrainAs(button1) {
            top.linkTo(parent.top, margin = 16.dp)
        }) {
            Text(text = "Button1")
        }

        Text(text = "Text", modifier = Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            //ConstraintLayout中水平居中
            centerAround(button1.end)
        })
    }
}