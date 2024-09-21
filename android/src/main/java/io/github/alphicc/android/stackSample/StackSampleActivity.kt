package io.github.alphicc.android.stackSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.ui.ComponentsContainer

val stackSampleRouter: TreeRouter = TreeRouter.new()

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
