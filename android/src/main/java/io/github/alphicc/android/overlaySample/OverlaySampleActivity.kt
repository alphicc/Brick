package io.github.alphicc.android.overlaySample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alphicc.brick.TreeRouter
import com.alphicc.brick.navigationContainers.ScreensContainer
import io.github.alphicc.android.overlaySample.listScreen.listScreen

val overlaySampleRouter = TreeRouter.new()

class OverlaySampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreensContainer(overlaySampleRouter)
        }

        if (savedInstanceState == null) {
            overlaySampleRouter.addScreen(listScreen, overlaySampleRouter)
        }
    }
}
