package com.navigationtestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import java.util.*

private val router: TreeRouter = TreeRouterImpl()

val screen1 = Screen(
    key = "1",
    content = { SimpleScreen(1, "new") { router.addScreen(screen2) } }
)

val screen2 = Screen(
    key = "2",
    content = { SimpleScreen(2, "new") { router.addScreen(screen3) } }
)

val screen3 = Screen(
    key = "3",
    content = { SimpleScreen(3, "new") { router.addScreen(screen4) } }
)

val screen4 = Screen(
    key = "4",
    content = { SimpleScreen(4, "replace") { router.replaceScreen(screen5) } }
)

val screen5 = Screen(
    key = "5",
    content = { SimpleScreen(5, "addChild") { router.addChild(screenChild1) } }
)

val screenChild1 = Screen(
    key = "C1",
    content = { Child(10, "newChild") { router.addChild(screenChild2) } }
)

val screenChild2 = Screen(
    key = "C2",
    content = { Child(20, "newChild") { router.addChild(screenChild3) } }
)

val screenChild3 = Screen(
    key = "C3",
    content = { Child(30, "newChild") { router.addChild(screenChild4) } }
)

val screenChild4 = Screen(
    key = "C4",
    content = { Child(40, "replaceChild") { router.replaceChild(screenChild5) } }
)

val screenChild5 = Screen(
    key = "C5",
    content = { ChildExt(50, "backToChild") { router.backToChild("C1") } }
)

class StackNavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreensContainer(router)
        }

        if (savedInstanceState == null) {
            router.addScreen(screen1)
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
fun ChildExt(int: Int, actionTitle: String, action: () -> Unit) {

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