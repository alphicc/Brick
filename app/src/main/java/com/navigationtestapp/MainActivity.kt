package com.navigationtestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


val router: ThreeRouter = ThreeRouterImpl()

val screen1 = Screen(
    key = "1",
    content = { Container(1, "new") { router.new(screen2) } }
)

val screen2 = Screen(
    key = "2",
    content = { Container(2, "new") { router.new(screen3) } }
)

val screen3 = Screen(
    key = "3",
    content = { Container(3, "new") { router.new(screen4) } }
)

val screen4 = Screen(
    key = "4",
    content = { Container(4, "replace") { router.replace(screen5) } }
)

val screen5 = Screen(
    key = "5",
    content = { Container(5, "back") { router.backTo("1") } }
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NavigationContainer(this, router)
        router.new(screen1)
    }
}

@Composable
fun Container(int: Int, actionTitle: String, action: () -> Unit) {

    val color = remember {
        val rnd = java.util.Random()
        val color =
            android.graphics.Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        Color(color)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = int.toString()
        )

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = action
        ) {
            Text(text = actionTitle)
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    NavigationTestAppTheme {
//        Greeting("Android")
//    }
//}