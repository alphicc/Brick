package io.github.alphicc.android.stackSample.productBuySuccess.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductBuySuccessDialog(
    dialogShowed: Boolean,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val alpha by animateFloatAsState(targetValue = if (dialogShowed) 1f else 0f, label = "")

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(Color.Gray.copy(.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(250.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                text = "Operation Success",
                fontSize = 18.sp,
                fontWeight = FontWeight.W600
            )
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                onClick = onDoneClicked
            ) {
                Text(text = "Submit")
            }
        }
    }
}