package io.github.alphicc.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.github.alphicc.android.childSample.ChildSampleActivity
import io.github.alphicc.android.childSample.childSampleRouter
import io.github.alphicc.android.compositeSample.CompositeSampleActivity
import io.github.alphicc.android.compositeSample.component1
import io.github.alphicc.android.compositeSample.component2
import io.github.alphicc.android.compositeSample.component3
import io.github.alphicc.android.compositeSample.compositeSampleRouter
import io.github.alphicc.android.compositeSample.compositeScreen
import io.github.alphicc.android.largeSample.LargeSampleActivity
import io.github.alphicc.android.largeSample.largeSampleRouter
import io.github.alphicc.android.largeSample.screens.Screens
import io.github.alphicc.android.overlaySample.OverlaySampleActivity
import io.github.alphicc.android.overlaySample.listScreen.listScreen
import io.github.alphicc.android.overlaySample.overlaySampleRouter
import io.github.alphicc.android.stackSample.StackSampleActivity
import io.github.alphicc.android.stackSample.screen1
import io.github.alphicc.android.stackSample.stackSampleRouter
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    stackSampleRouter.cleanRouter()
                    stackSampleRouter.addComponent(screen1)
                    stackSampleRouter.proceedAction {
                        context.startActivity(Intent(context, StackSampleActivity::class.java))
                    }
                }) {
                    Text(text = "Stack navigation")
                }

                Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                    childSampleRouter.cleanRouter()
                    childSampleRouter.addComponent(io.github.alphicc.android.childSample.screen1)
                    childSampleRouter.proceedAction {
                        context.startActivity(Intent(context, ChildSampleActivity::class.java))
                    }
                }) {
                    Text(text = "Child navigation")
                }

                Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                    largeSampleRouter.cleanRouter()
                    largeSampleRouter.addComponent(Screens.welcomeScreen, largeSampleRouter)
                    largeSampleRouter.proceedAction {
                        context.startActivity(Intent(context, LargeSampleActivity::class.java))
                    }
                }) {
                    Text(text = "Nested navigation")
                }

                Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                    overlaySampleRouter.cleanRouter()
                    overlaySampleRouter.addComponent(listScreen, overlaySampleRouter)
                    overlaySampleRouter.proceedAction {
                        context.startActivity(Intent(context, OverlaySampleActivity::class.java))
                    }
                }) {
                    Text(text = "Overlay")
                }

                Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                    compositeSampleRouter.cleanRouter()
                    compositeSampleRouter.addComponent(compositeScreen)
                    compositeSampleRouter.attachCompositeComponent(component1, "321")
                    compositeSampleRouter.attachCompositeComponent(component2)
                    compositeSampleRouter.attachCompositeComponent(component3)
                    compositeSampleRouter.proceedAction {
                        context.startActivity(Intent(context, CompositeSampleActivity::class.java))
                    }
                }) {
                    Text(text = "Composite navigation")
                }
            }
        }
    }
}
