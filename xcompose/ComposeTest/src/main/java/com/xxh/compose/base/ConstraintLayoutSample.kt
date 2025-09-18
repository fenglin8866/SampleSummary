package com.xxh.compose.base

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

@Composable
fun ConstraintLayoutSample() {
    Column {
        ConstraintLayoutContent()
        ConstraintLayoutContent2()
        ConstraintLayoutContent3()
        ConstraintLayoutContent4()
    }
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
        val (button1, button2, text) = createRefs()

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

        val barrier = createEndBarrier(button1, text)

        Button(onClick = {}, modifier = Modifier.constrainAs(button2) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(barrier)
        }) {
            Text(text = "Button2")
        }
    }
}

@Composable
fun ConstraintLayoutContent3() {
    ConstraintLayout {
        //通过createRefs创建引用，ConstraintLayout中的每一个元素都需要关联一个引用
        val text = createRef()
        val guideline = createGuidelineFromStart(0.5f)
        Text(
            text = "Text ConstraintLayoutContent3 ConstraintLayoutContent3 ConstraintLayoutContent3 ConstraintLayoutContent3",
            modifier = Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                top.linkTo(parent.top)
                width = Dimension.preferredWrapContent
            },
        )
    }
}

@Composable
fun ConstraintLayoutContent4() {
    BoxWithConstraints {
        val constraintSet = MyConstraintSet(if (maxHeight > maxWidth) 16.dp else 32.dp)
        ConstraintLayout(constraintSet) {
            Button(onClick = {}, modifier = Modifier.layoutId("button")) {
                Text(text = "Button")
            }
            Text(text = "Text", modifier = Modifier.layoutId("text"))
        }
    }
}


@Composable
fun MyConstraintSet(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")
        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin = margin)
            centerHorizontallyTo(parent)
        }
    }
}
