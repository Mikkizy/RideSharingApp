package com.ukaka.ridesharingapp.presentation.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ukaka.ridesharingapp.common.Dimensions
import com.ukaka.ridesharingapp.ui.theme.RideSharingAppTheme

@Composable
fun DestinationItem(
    destination: String,
    onLocationSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onLocationSelected() }
            .background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier
                .padding(start = Dimensions.pagePadding.div(2)),
            text = destination,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body1
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            thickness = 2.dp,
            color = Color.DarkGray
        )
    }
}

@Preview
@Composable
fun DestinationItemPreview() {
    RideSharingAppTheme {
        DestinationItem(destination = "Lawanson Road, NIgeria") {

        }
    }
}