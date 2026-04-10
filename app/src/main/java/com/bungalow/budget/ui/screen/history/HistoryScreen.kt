package com.bungalow.budget.ui.screen.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.R
import com.bungalow.budget.ui.composable.BudgetSummary
import com.bungalow.budget.ui.composable.NavigationToolbar
import com.bungalow.budget.utils.getMockedBudget
import com.investigate.domain.model.Budget

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()

    Content(
        budgets = budgets,
        onBackClick = onBackClick
    )
}

@Composable
private fun Content(
    budgets: List<Budget>,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NavigationToolbar(
            titleResId = R.string.start_budget_title,
            onBackClick = onBackClick
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(budgets) { budget ->
                BudgetSummary(
                    budget = budget,
                    onBudgetSettingsClick = { }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryPreview() {
    Content(
        budgets = listOf(getMockedBudget(), getMockedBudget()),
        onBackClick = {}
    )
}