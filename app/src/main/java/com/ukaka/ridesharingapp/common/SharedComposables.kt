package com.ukaka.ridesharingapp.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ukaka.ridesharingapp.R
import com.ukaka.ridesharingapp.ui.theme.typography

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    subtitleText: String = "Sign up for free"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_header),
            style = typography.h2
        )
        Text(
            text = subtitleText,
            style = typography.subtitle2
        )
    }
}