@file:OptIn(ExperimentalMaterial3Api::class)

package com.bungalow.budget.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NavigationToolbar(
    titleResId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(titleResId),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(
                onClick = { /* do nothing */ },
                modifier = Modifier
                    .alpha(0f)
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        modifier = modifier
    )
}