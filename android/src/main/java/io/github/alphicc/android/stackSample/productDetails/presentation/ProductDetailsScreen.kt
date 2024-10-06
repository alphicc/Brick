package io.github.alphicc.android.stackSample.productDetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductDetailsScreen(
    productIndex: Int,
    onBlackClicked: () -> Unit,
    onBuyProductClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProductLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                onBlackClicked = onBlackClicked,
                productIndex = productIndex
            )

            Text(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                text = "Product Name $productIndex",
                fontSize = 24.sp,
                fontWeight = FontWeight.W700
            )

            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                text = "Product description $productIndex".repeat(10),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp)
                .height(52.dp)
                .fillMaxWidth(),
            onClick = onBuyProductClicked
        ) {
            Text(text = "Add to cart")
        }
    }
}

@Composable
private fun ProductLogo(
    productIndex: Int,
    onBlackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Product Logo $productIndex"
            )
        }

        Text(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onBlackClicked),
            text = "Back"
        )
    }
}