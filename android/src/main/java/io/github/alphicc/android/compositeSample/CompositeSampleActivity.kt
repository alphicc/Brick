package io.github.alphicc.android.compositeSample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alphicc.brick.AndroidScreensContainer
import com.alphicc.brick.Screen
import com.alphicc.brick.TreeRouter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

val compositeSampleRouter: TreeRouter = TreeRouter.new()

val compositeScreenInternal1 = Screen(
    key = "CompositeScreenInternal 1",
    onCreate = { _, argument ->
        Log.d("Alpha", "onCreate CompositeScreenInternal 1 ${argument.get<String>()}")
        "00000"
    },
    onDestroy = { Log.d("Alpha", "onDestroy CompositeScreenInternal 1 ${it.get<String>()}") },
    content = { _, _ ->
        DisposableEffect(null) {
            Log.d("Alpha", "DisposableEffect Internal 1")
            onDispose {
                Log.d("Alpha", "onDispose Internal 1")
            }
        }
        Text("CompositeScreenInternal 1")
    }
)

val compositeScreenInternal2 = Screen<Unit>(
    key = "CompositeScreenInternal 2",
    onCreate = { connector, _ ->
        connector.onEach {
            Log.d("Alpha", "Data receive in CompositeScreenInternal 2 ${it.get<Int>()}")
        }.launchIn(GlobalScope)
        Log.d("Alpha", "onCreate CompositeScreenInternal 2")
    },
    onDestroy = { Log.d("Alpha", "onDestroy CompositeScreenInternal 2") },
    content = { _, _ ->
        DisposableEffect(null) {
            Log.d("Alpha", "DisposableEffect Internal 2")
            onDispose {
                Log.d("Alpha", "onDispose Internal 2")
            }
        }
        Text("CompositeScreenInternal 2")
    }
)

val compositeScreenInternal3 = Screen<Unit>(
    key = "CompositeScreenInternal 3",
    onCreate = { _, _ -> Log.d("Alpha", "onCreate CompositeScreenInternal 3") },
    onDestroy = { Log.d("Alpha", "onDestroy CompositeScreenInternal 3") },
    content = { _, _ ->
        DisposableEffect(null) {
            Log.d("Alpha", "DisposableEffect Internal 3")
            onDispose {
                Log.d("Alpha", "onDispose Internal 3")
            }
        }
        Button(
            {
                GlobalScope.launch {
                    compositeSampleRouter.passArgument(compositeScreenInternal2.key, 123)
                }
            }
        ) {
            Text("CompositeScreenInternal 3")
        }
    }
)

val compositeScreen = Screen<Unit>(
    key = "CompositeScreen",
    onCreate = { _, _ -> Log.d("Alpha", "onCreate CompositeScreen") },
    onDestroy = { Log.d("Alpha", "onDestroy CompositeScreen") },
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