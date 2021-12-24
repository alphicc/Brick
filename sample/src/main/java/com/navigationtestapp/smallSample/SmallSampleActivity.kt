package com.navigationtestapp.smallSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.ScreensContainer
import java.util.*

val smallSampleRouter: TreeRouter = TreeRouter.new()

val screen1 = Screen<Unit>(
    key = "1",
    content = { SimpleScreen(1, "new") { smallSampleRouter.addScreen(screen2) } }
)

val screen2 = Screen<Unit>(
    key = "2",
    content = { SimpleScreen(2, "new") { smallSampleRouter.addScreen(screen3) } }
)

val screen3 = Screen<Unit>(
    key = "3",
    content = { SimpleScreen(3, "new") { smallSampleRouter.addScreen(screen4) } }
)

val screen4 = Screen<Unit>(
    key = "4",
    content = { SimpleScreen(4, "replace") { smallSampleRouter.replaceScreen(screen5) } }
)

val screen5 = Screen<Unit>(
    key = "5",
    content = { SimpleScreen(5, "addChild") { smallSampleRouter.addChild(screenChild1) } }
)

val screenChild1 = Screen<Unit>(
    key = "C1",
    content = { Child(10, "newChild") { smallSampleRouter.addChild(screenChild2) } }
)

val screenChild2 = Screen<Unit>(
    key = "C2",
    content = { Child(20, "newChild") { smallSampleRouter.addChild(screenChild3) } }
)

val screenChild3 = Screen<Unit>(
    key = "C3",
    content = { Child(30, "newChild") { smallSampleRouter.addChild(screenChild4) } }
)

val screenChild4 = Screen<Unit>(
    key = "C4",
    content = { Child(40, "replaceChild") { smallSampleRouter.replaceChild(screenChild5) } }
)

val screenChild5 = Screen<Unit>(
    key = "C5",
    content = { ChildExt(50) }
)

@ExperimentalAnimationApi
class SmallSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreensContainer(smallSampleRouter)
        }

        if (savedInstanceState == null) {
            smallSampleRouter.addScreen(screen1)
        }
    }
}

@Composable
private fun SimpleScreen(int: Int, actionTitle: String, action: () -> Unit) {

    val color = remember {
        val rnd = Random()
        val color =
            android.graphics.Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        Color(color)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = int.toString()
        )

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = action
        ) {
            Text(text = actionTitle)
        }
    }
}

@Composable
fun Child(int: Int, actionTitle: String, action: () -> Unit) {

    val color = remember {
        val rnd = Random()
        val color =
            android.graphics.Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        Color(color)
    }

    Box(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
            .background(color = color)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = int.toString()
        )

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = action
        ) {
            Text(text = actionTitle)
        }
    }
}


@Composable
fun ChildExt(int: Int) {

    val color = remember {
        val rnd = Random()
        val color =
            android.graphics.Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        Color(color)
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(64.dp)
            .background(color = color)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = int.toString()
        )
    }
}