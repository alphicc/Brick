package io.github.alphicc.android.largeSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.ui.ComponentsContainer
import io.github.alphicc.android.largeSample.screens.Screens.welcomeScreen
import kotlinx.coroutines.launch

val largeSampleRouter: TreeRouter = TreeRouter.new()

@ExperimentalAnimationApi
class LargeSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComponentsContainer(
                containerConnector = largeSampleRouter,
                onRouterEmpty = {
                    println("onRouterEmpty Finish NOW")
                    finish()
                }
            )

            Button(
                modifier = Modifier.padding(top = 48.dp),
                onClick = {
                    lifecycleScope.launch {
                        println("cleanRouter NOW")
                        largeSampleRouter.cleanRouter()
                    }
                }
            ) {
                Text(text = "CleanRouter")
            }
        }
    }
}