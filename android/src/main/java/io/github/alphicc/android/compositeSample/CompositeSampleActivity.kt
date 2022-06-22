package io.github.alphicc.android.compositeSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alphicc.brick.AndroidScreensContainer
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter

val compositeSampleRouter: TreeRouter = TreeRouter.new()

val compositeScreenInternal1 = Screen<Unit>(
    key = "CompositeScreenInternal 1",
    content = { _, _ -> Text("CompositeScreenInternal 1") }
)

val compositeScreenInternal2 = Screen<Unit>(
    key = "CompositeScreenInternal 2",
    content = { _, _ -> Text("CompositeScreenInternal 2") }
)

val compositeScreenInternal3 = Screen<Unit>(
    key = "CompositeScreenInternal 3",
    content = { _, _ ->
        Button({}) {
            Text("CompositeScreenInternal 3")
        }
    }
)

val compositeScreen = Screen<Unit>(
    key = "CompositeScreen",
    content = { _, compositeContainer ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                compositeContainer.place(compositeScreenInternal1.key)
            }

            Box(modifier = Modifier.align(Alignment.Center)) {
                compositeContainer.place(compositeScreenInternal3.key)
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                compositeContainer.place(compositeScreenInternal2.key)
            }
        }
    }
)


@ExperimentalAnimationApi
class CompositeSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidScreensContainer(compositeSampleRouter) {
                finish()
            }
        }

        if (savedInstanceState == null) {
            compositeSampleRouter.addScreen(compositeScreen)
            compositeSampleRouter.attachCompositeScreen(compositeScreenInternal1, "321")
            compositeSampleRouter.attachCompositeScreen(compositeScreenInternal2)
            compositeSampleRouter.attachCompositeScreen(compositeScreenInternal3)
        }
    }
}