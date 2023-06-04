package com.ukaka.ridesharingapp.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ukaka.ridesharingapp.common.Dimensions

@Composable
fun TopBar(
    title: String,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    onBackClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = Dimensions.pagePadding.div(2))
    ) {
        IconButton(
            onClick = { onBackClicked() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.Center),
            color = contentColor,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}