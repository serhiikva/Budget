package com.bungalow.budget.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bungalow.budget.R
import com.bungalow.budget.ui.theme.ProgressDanger
import com.bungalow.budget.ui.theme.ProgressSafe
import com.bungalow.budget.ui.theme.ProgressWarning
import com.bungalow.budget.utils.getAmount
import com.bungalow.budget.utils.getMockedBudget
import com.bungalow.budget.utils.getSpentAmount
import com.bungalow.budget.utils.toLocalDateTime
import com.investigate.domain.model.Budget

@Composable
fun BudgetSummary(
    budget: Budget,
    onBudgetSettingsClick: (Budget) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
    val offsetY by animateDpAsState(targetValue = if (!isExpanded) 0.dp else 20.dp)
    val budgetSpentAmount = budget.getSpentAmount()
    val budgetAmount = budget.getAmount()

    val progress = (budgetSpentAmount.toFloat() / budgetAmount).coerceIn(0f, 1f)

    val progressColor = when {
        progress < 0.6f -> ProgressSafe
        progress < 0.9f -> ProgressWarning
        else -> ProgressDanger
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.home_budget_summary),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                ) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Toggle expand",
                        modifier = Modifier.graphicsLayer { rotationZ = rotation }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BudgetProgress(
                progressColor = progressColor,
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                content = {
                    AnimatedProgressText(
                        value = budgetSpentAmount.toFloat(),
                        maxValue = budgetAmount.toFloat()
                    )
                },
                contentModifier = Modifier
                    .fillMaxSize()
                    .offset(y = offsetY)
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Started at: ${budget.startDateMillis.toLocalDateTime()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (budget.endDateMillis > 0) {
                            Text(
                                text = "End at: ${budget.endDateMillis.toLocalDateTime()}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "$budgetSpentAmount spent",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Budget: $budgetAmount",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    FilledTonalButton(
                        onClick = { onBudgetSettingsClick(budget) },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Budget"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BudgetSummaryPreview() {
    BudgetSummary(
        budget = getMockedBudget(),
        onBudgetSettingsClick = {},
    )
}