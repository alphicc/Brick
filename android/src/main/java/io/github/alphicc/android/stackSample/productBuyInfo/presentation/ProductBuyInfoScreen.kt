package io.github.alphicc.android.stackSample.productBuyInfo.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductBuyInfoScreen(
    productIndex: Int,
    onBackClicked: () -> Unit,
    onBuyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Cart",
                fontSize = 34.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(horizontal = 64.dp)
                    .padding(vertical = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )

            CartPosition(
                modifier = Modifier.fillMaxWidth(),
                title = "Product Name $productIndex",
                price = "500"
            )

            CartPosition(
                modifier = Modifier.fillMaxWidth(),
                title = "Delivery",
                price = "100"
            )

            CartPosition(
                modifier = Modifier.fillMaxWidth(),
                title = "Service",
                price = "10"
            )

            CartPosition(
                modifier = Modifier.fillMaxWidth(),
                title = "Total price",
                price = "610"
            )
        }

        Text(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onBackClicked),
            text = "Back"
        )

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp)
                .height(52.dp)
                .fillMaxWidth(),
            onClick = onBuyClicked
        ) {
            Text(text = "Buy product")
        }
    }
}

@Composable
private fun CartPosition(title: String, price: String, modifier: Modifier = Modifier) {

    Box(modifier = modifier.height(52.dp)) {

        Text(modifier = Modifier.align(Alignment.CenterStart), text = title)

        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            fontWeight = FontWeight.W500,
            text = "$price coins"
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter)
        )
    }
}