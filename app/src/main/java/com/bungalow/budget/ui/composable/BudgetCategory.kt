package com.bungalow.budget.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bungalow.budget.ui.theme.ProgressDanger
import com.bungalow.budget.ui.theme.ProgressSafe
import com.bungalow.budget.ui.theme.ProgressWarning
import com.bungalow.budget.utils.getSpentAmount
import com.bungalow.budget.utils.toSentenceCase
import com.investigate.domain.model.BudgetCategory
import kotlinx.coroutines.launch

@Composable
fun BudgetCategory(
    category: BudgetCategory,
    onActionButtonClick: (BudgetCategory) -> Unit,
    onCategoryClick: (BudgetCategory) -> Unit,
    actionButtonContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val budgetSpentAmount = category.getSpentAmount()
    val budgetAmount = category.budgetAmount
    val progress = (budgetSpentAmount.toFloat() / budgetAmount).coerceIn(0f, 1f)

    val progressColor = when {
        progress < 0.6f -> ProgressSafe
        progress < 0.9f -> ProgressWarning
        else -> ProgressDanger
    }

    val animatedProgress = remember { Animatable(progress) }

    LaunchedEffect(budgetSpentAmount) {
        animatedProgress.animateTo(
            targetValue = budgetSpentAmount.toFloat() / budgetAmount,
            animationSpec = tween(1000)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = category.name.toSentenceCase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickable(onClick = { onCategoryClick(category) })
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(50))
                        .background(progressColor.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress.value)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .background(progressColor)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedProgressText(
                            value = budgetSpentAmount.toFloat(),
                            maxValue = budgetAmount.toFloat(),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = { onActionButtonClick(category) },
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(6.dp),
                    content = actionButtonContent
                )
            }
        }
    }
}