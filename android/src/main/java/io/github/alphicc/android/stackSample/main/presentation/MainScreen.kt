package io.github.alphicc.android.stackSample.main.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
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
import androidx.compose.ui.unit.sp
import com.alphicc.brick.ui.ComponentsContainer
import com.alphicc.brick.ui.ContainerConnector

@Composable
fun MainScreen(
    indexMenu: Int,
    login: String,
    containerConnector: ContainerConnector?,
    onBackClicked: () -> Unit,
    onMenuClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(bottom = 80.dp)
                .fillMaxSize()
        ) {
            if (containerConnector != null) {
                ComponentsContainer(containerConnector)
            }
        }

        Surface(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            elevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(.1f)),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ButtonMenu(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(64.dp),
                    color = Color.Red,
                    title = "Orders",
                    isSelected = indexMenu == 0,
                    onClick = { onMenuClicked(0) }
                )

                ButtonMenu(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(64.dp),
                    color = Color.Green,
                    title = "Basket",
                    isSelected = indexMenu == 1,
                    onClick = {}
                )

                ButtonMenu(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(64.dp),
                    color = Color.Blue,
                    title = login,
                    isSelected = indexMenu == 2,
                    onClick = { onMenuClicked(2) }
                )
            }
        }
    }

    BackHandler {
        onBackClicked()
    }
}

@Composable
private fun ButtonMenu(
    color: Color,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .background(color)
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            text = title,
            maxLines = 1,
            fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W400,
            overflow = TextOverflow.Ellipsis
        )
    }
}