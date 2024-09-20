package io.github.alphicc.android.stackSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.ui.ComponentsContainer
import java.util.Random

val stackSampleRouter: TreeRouter = TreeRouter.new()

val screen1: Component<Unit> = Component(
    key = "1",
    descriptor = Descriptor.Screen.Default,
    onCreate = { _, _ -> println("OnCreate screen1") },
    onDestroy = { _ -> println("onDestroy screen1") },
    content = { _, _ ->
        SimpleScreen(1, "Add Screen2") {
            stackSampleRouter.addComponent(screen2)
        }
    })

val screen2: Component<Unit> = Component(
    key = "2",
    descriptor = Descriptor.Screen.Animated.SwipeBack,
    onCreate = { _, _ -> println("OnCreate Screen2") },
    onDestroy = { _ -> println("onDestroy Screen2") },
    content = { _, _ ->
        SimpleScreen(2, "Add Screen3") { stackSampleRouter.addComponent(screen3) }
    })

val screen3: Component<Unit> = Component(
    key = "3",
    descriptor = Descriptor.Screen.Animated.SwipeBack,
    onCreate = { _, _ -> println("OnCreate screen3") },
    onDestroy = { _ -> println("onDestroy screen3") },
    content = { _, _ ->
        SimpleScreen(3, "Add Screen4") { stackSampleRouter.addComponent(screen4) }
    })

val screen4: Component<Unit> = Component(
    key = "4",
    descriptor = Descriptor.Screen.Animated.CrossFade,
    onCreate = { _, _ -> println("OnCreate screen4") },
    onDestroy = { _ -> println("onDestroy screen4") },
    content = { _, _ ->
        SimpleScreen(4, "Add Screen5") { stackSampleRouter.addComponent(screen5) }
    })

val screen5: Component<Unit> = Component(
    key = "5",
    descriptor = Descriptor.Screen.Animated.SwipeBack,
    onCreate = { _, _ -> println("OnCreate screen5") },
    onDestroy = { _ -> println("onDestroy screen5") },
    content = { _, _ ->
        SimpleScreen(5, "BackTo Screen1") { stackSampleRouter.backToComponent(screen1.key) }
    })

@ExperimentalAnimationApi
class StackSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComponentsContainer(stackSampleRouter) {
                finish()
            }
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
            modifier = Modifier.align(Alignment.Center), text = int.toString()
        )

        Button(
            modifier = Modifier.align(Alignment.BottomCenter), onClick = action
        ) {
            Text(text = actionTitle)
        }
    }
}
