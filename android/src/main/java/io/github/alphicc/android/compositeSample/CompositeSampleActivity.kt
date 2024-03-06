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
import com.alphicc.brick.Component
import com.alphicc.brick.Descriptor
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.ui.ComponentsContainer

val compositeSampleRouter: TreeRouter = TreeRouter.new()

val component1 = Component<Unit>(
    key = "CompositeScreenInternal 1",
    descriptor = Descriptor.Composite,
    content = { _, _ -> Text("CompositeScreenInternal 1") }
)

val component2 = Component<Unit>(
    key = "CompositeScreenInternal 2",
    descriptor = Descriptor.Composite,
    content = { _, _ -> Text("CompositeScreenInternal 2") }
)

val component3 = Component<Unit>(
    key = "CompositeScreenInternal 3",
    descriptor = Descriptor.Composite,
    content = { _, _ ->
        Button({}) {
            Text("CompositeScreenInternal 3")
        }
    }
)

val compositeScreen = Component<Unit>(
    key = "CompositeScreen",
    descriptor = Descriptor.Main.Default,
    content = { _, compositeContainer ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                compositeContainer.place(component1.key)
            }

            Box(modifier = Modifier.align(Alignment.Center)) {
                compositeContainer.place(component3.key)
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                compositeContainer.place(component2.key)
            }
        }
    }
)


@ExperimentalAnimationApi
class CompositeSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComponentsContainer(compositeSampleRouter) {
                finish()
            }
        }

        if (savedInstanceState == null) {
            compositeSampleRouter.addComponent(compositeScreen)
            compositeSampleRouter.attachCompositeComponent(component1, "321")
            compositeSampleRouter.attachCompositeComponent(component2)
            compositeSampleRouter.attachCompositeComponent(component3)
        }
    }
}