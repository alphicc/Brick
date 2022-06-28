package io.github.alphicc.android.overlaySample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alphicc.brick.AndroidComponentsContainer
import com.alphicc.brick.TreeRouter
import io.github.alphicc.android.overlaySample.listScreen.listScreen

val overlaySampleRouter = TreeRouter.new()

class OverlaySampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidComponentsContainer(overlaySampleRouter) {
                finish()
            }
        }

        if (savedInstanceState == null) {
            overlaySampleRouter.addComponent(listScreen, overlaySampleRouter)
        }
    }
}
