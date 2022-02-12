package io.github.alphicc.android.largeSample.screens.childNavigationSample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ChildNavigationContent(viewModel: ChildNavigationViewModel) {

    Box(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text = "Inner navigation screen TWO")

        Button(onClick = viewModel::onBackClicked) {
            Text(text = "GO BACK")
        }

        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
            Button(onClick = viewModel::onForwardClicked) {
                Text(text = "FORWARD CHILD")
            }
            Button(onClick = viewModel::onForwardTwoClicked) {
                Text(text = "FORWARD CHILD TWO")
            }
        }
    }
}