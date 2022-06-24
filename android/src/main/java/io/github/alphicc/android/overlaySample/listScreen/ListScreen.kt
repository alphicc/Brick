package io.github.alphicc.android.overlaySample.listScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alphicc.brick.Component
import com.alphicc.brick.TreeRouter

val listScreen = Component(
    key = "listScreen",
    onCreate = { _, argument ->
        val router = argument.get<TreeRouter>()
        return@Component ListViewModel(router)
    },
    content = { dataContainer, _ ->
        val viewModel = dataContainer.get<ListViewModel>()
        ListScreenContent(viewModel)
    }
)

@Composable
private fun ListScreenContent(viewModel: ListViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            IconButton(onClick = viewModel::onOpenNavigationDrawerClicked) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Navigation drawer menu")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = 25,
                key = { index -> "index$index" },
                itemContent = { index ->

                    val boxColor = if (index % 2 == 0) Color.Blue
                    else Color.Red

                    Box(
                        modifier = Modifier
                            .background(color = boxColor.copy(alpha = 0.2f))
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(16.dp),
                            text = "Element position = $index"
                        )
                    }
                }
            )
        }
    }
}