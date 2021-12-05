package com.navigationtestapp

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
import com.navigationtestapp.largeSample.LargeSampleActivity
import com.navigationtestapp.largeSample.largeSampleRouter
import com.navigationtestapp.smallSample.SmallSampleActivity
import com.navigationtestapp.smallSample.smallSampleRouter

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
                    smallSampleRouter.cleanRouter()
                    context.startActivity(Intent(context, SmallSampleActivity::class.java))
                }) {
                    Text(text = "Stack navigation")
                }

                Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                    largeSampleRouter.cleanRouter()
                    context.startActivity(Intent(context, LargeSampleActivity::class.java))
                }) {
                    Text(text = "Nested navigation")
                }
            }
        }
    }
}