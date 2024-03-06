import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeUIViewController
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.ui.ComponentsContainer
import platform.UIKit.UIViewController

val router: TreeRouter by lazy {
    TreeRouter.new().apply {
        addComponent(screen1)
    }
}

val screen1 by lazy {
    Component<Unit>(
        key = "1",
        descriptor = Descriptor.Main.RetainUi.CrossFade,
        content = { _, _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.DarkGray)
            ) {

                Text(
                    modifier = Modifier.align(Alignment.Center), text = "Bottom Screen"
                )

                Button(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = { router.addComponent(screen2) }
                ) {
                    Text(text = "Add screen")
                }
            }
        })
}

val screen2 by lazy {
    Component<Unit>(
        key = "2",
        descriptor = Descriptor.Main.RetainUi.SwipeBack,
        content = { _, _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Magenta)
            ) {

                Text(
                    modifier = Modifier.align(Alignment.Center), text = "Top Screen"
                )
            }
        })
}

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        ComponentsContainer(
            router
        )
    }
}