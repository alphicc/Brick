package io.github.alphicc.android.stackSample.productsScreen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ProductsScreen(modifier: Modifier = Modifier, onProductClicked: (Int) -> Unit) {
    Box(modifier = modifier.background(color = Color.White)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(100) { index ->
                ProductItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable(
                            onClick = { onProductClicked(index) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true)
                        )
                        .padding(horizontal = 16.dp),
                    index = index
                )
            }
        }
    }
}

@Composable
private fun ProductItem(modifier: Modifier = Modifier, index: Int) {
    Box(modifier = modifier) {
        ProductLogo(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterStart),
            index = index
        )

        Column(
            modifier = Modifier
                .padding(start = 80.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                text = "Product Name $index"
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                text = "Very very very very Long Product Description $index"
            )
        }

        Divider(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun ProductLogo(modifier: Modifier = Modifier, index: Int) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Gray)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            text = "Product Logo $index"
        )
    }
}