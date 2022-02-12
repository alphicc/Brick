import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alphicc.brick.DesktopScreensContainer
import com.alphicc.brick.TreeRouter
import io.github.alphicc.desktop.Screens.welcomeScreen

val router: TreeRouter = TreeRouter.new().apply {
    addScreen(welcomeScreen, this)
}

@ExperimentalAnimationApi
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            DesktopScreensContainer(router)
        }
    }
}