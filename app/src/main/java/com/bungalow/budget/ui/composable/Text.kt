package com.bungalow.budget.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedProgressText(
    value: Float,
    maxValue: Float,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val animatedSpentAmount = remember { Animatable(value) }

    LaunchedEffect(value) {
        animatedSpentAmount.animateTo(
            targetValue = value,
            animationSpec = tween(1000)
        )
    }

    Text(
        text = "${animatedSpentAmount.value.toInt()} / ${maxValue.toInt()}",
        style = textStyle,
        color = textColor,
        modifier = modifier
    )
}