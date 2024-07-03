import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeUIViewController
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.ui.ComponentsContainer
import kotlinx.coroutines.flow.filterIsInstance
import platform.UIKit.UIViewController
import kotlin.random.Random

private val router: TreeRouter by lazy {
    TreeRouter.new().apply {
        addComponent(screen1)
    }
}

val screen1: Component<Unit> = Component(
    key = "1",
    descriptor = Descriptor.Main.RetainUi.CrossFade,
    onCreate = { _, _ -> println("OnCreate screen1") },
    onDestroy = { _ -> println("onDestroy screen1") },
    content = { _, _ ->
        SimpleScreen(1, "add") {
            router.broadcastArgumentsFlow.filterIsInstance<String>()
            router.addComponent(screen2)
        }
    })

val screen2: Component<Unit> = Component(
    key = "2",
    descriptor = Descriptor.Main.RetainUi.CrossFade,
    onCreate = { _, _ -> println("OnCreate Screen2") },
    onDestroy = { _ -> println("onDestroy Screen2") },
    content = { _, _ ->
        SimpleScreen(2, "replace") { router.replaceComponent(screen3) }
    })

val screen3: Component<Unit> = Component<Unit>(
    key = "3",
    descriptor = Descriptor.Main.RetainUi.CrossFade,
    onCreate = { _, _ -> println("OnCreate screen3") },
    onDestroy = { _ -> println("onDestroy screen3") },
    content = { _, _ ->
        SimpleScreen(3, "newRoot") { router.newRootComponent(screen4) }
    })

val screen4: Component<Unit> = Component<Unit>(
    key = "4",
    descriptor = Descriptor.Main.RetainUi.CrossFade,
    onCreate = { _, _ -> println("OnCreate screen4") },
    onDestroy = { _ -> println("onDestroy screen4") },
    content = { _, _ ->
        SimpleScreen(4, "add") { router.addComponent(screen5) }
    })

val screen5: Component<Unit> = Component<Unit>(
    key = "5",
    descriptor = Descriptor.Main.RetainUi.CrossFade,
    onCreate = { _, _ -> println("OnCreate screen5") },
    onDestroy = { _ -> println("onDestroy screen5") },
    content = { _, _ ->
        SimpleScreen(5, "backTO") { router.backToComponent(screen4.key) }
    })


fun MainViewController(): UIViewController = ComposeUIViewController {
    ComponentsContainer(
        router
    )
}

@Composable
private fun SimpleScreen(int: Int, actionTitle: String, action: () -> Unit) {

    val color = remember {
        Color(255, Random.nextInt(256), Random.nextInt(256))
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