package io.github.alphicc.android.largeSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alphicc.brick.AndroidAnimatedComponentsContainer
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.largeSample.screens.Screens.welcomeScreen
import kotlinx.coroutines.GlobalScope

val largeSampleRouter: TreeRouter = TreeRouter.new(GlobalScope)

@ExperimentalAnimationApi
class LargeSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidAnimatedComponentsContainer(
                containerConnector = largeSampleRouter,
                onRouterEmpty = { finish() },
                enterTransition = scaleIn(
                    initialScale = .90f,
                    animationSpec = tween(easing = LinearEasing)
                ) + fadeIn(),
                exitTransition = scaleOut(
                    targetScale = .90f,
                    animationSpec = tween(easing = LinearEasing)
                ) + fadeOut(),
            )

            Button(
                modifier = Modifier.padding(top = 48.dp),
                onClick = { largeSampleRouter.cleanRouter() }
            ) {
                Text(text = "CleanRouter")
            }
        }

        if (savedInstanceState == null) {
            largeSampleRouter.addComponent(welcomeScreen, largeSampleRouter)
        }
    }
}