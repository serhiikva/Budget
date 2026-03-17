package com.bungalow.budget.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.R
import com.bungalow.budget.ui.composable.BudgetSummary
import com.bungalow.budget.ui.dialog.add_payment.AddPaymentDialog
import com.bungalow.budget.ui.dialog.add_payment.AddPaymentViewModel
import com.bungalow.budget.ui.navigation.LocalAnimatedVisibilityScope
import com.bungalow.budget.ui.navigation.LocalSharedTransitionScope
import com.bungalow.budget.utils.getMockedBudget
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    addPaymentViewModel: AddPaymentViewModel = hiltViewModel(),
    onStartBudgetClick: () -> Unit,
    onBudgetSettingsClick: (Int) -> Unit,
    onCategoryClick: (Int, String) -> Unit,
) {
    val budget by viewModel.budget.collectAsStateWithLifecycle()
    val categoryToAddPayment by viewModel.categoryToAddPayment.collectAsStateWithLifecycle(null)

    Content(
        budget = budget,
        onStartBudgetClick = onStartBudgetClick,
        onAddCategoryPaymentClick = viewModel::onAddCategoryPaymentClick,
        onBudgetSettingsClick = onBudgetSettingsClick,
        onCategoryClick = onCategoryClick
    )

    LaunchedEffect(categoryToAddPayment) {
        categoryToAddPayment?.let { category ->
            addPaymentViewModel.resetInputs(category.id)
        }
    }

    categoryToAddPayment?.let { category ->
        AddPaymentDialog(
            viewModel = addPaymentViewModel,
            budgetCategory = category,
            onDismiss = viewModel::onAddCategoryPaymentDismissed,
            onConfirm = viewModel::onCategoryPaymentConfirmed
        )
    }
}

@Composable
private fun Content(
    budget: Budget,
    onStartBudgetClick: () -> Unit,
    onAddCategoryPaymentClick: (BudgetCategory) -> Unit,
    onBudgetSettingsClick: (Int) -> Unit,
    onCategoryClick: (Int, String) -> Unit
) {
    if (budget.id != 0) {
        BudgetDetails(
            budget = budget,
            onAddCategoryPaymentClick = onAddCategoryPaymentClick,
            onBudgetSettingsClick = onBudgetSettingsClick,
            onCategoryClick = onCategoryClick
        )
    } else {
        CreateBudget(
            onStartBudgetClick = onStartBudgetClick
        )
    }
}

@Composable
private fun BudgetDetails(
    budget: Budget,
    onAddCategoryPaymentClick: (BudgetCategory) -> Unit,
    onBudgetSettingsClick: (Int) -> Unit,
    onCategoryClick: (Int, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            onSettingsClick = {}
        )
        BudgetSummary(
            budget = budget,
            onBudgetSettingsClick = { onBudgetSettingsClick(it.id) }
        )
        CategoryList(
            budgetId = budget.id,
            categoryList = budget.categories,
            onAddCategoryPaymentClick = onAddCategoryPaymentClick,
            onCategoryClick = onCategoryClick
        )
    }
}

@Composable
private fun CreateBudget(
    onStartBudgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = onStartBudgetClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.home_start_budget))
        }
    }
}

@Composable
private fun Header(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(0.dp))
        IconButton(
            onClick = onSettingsClick,
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}

@Composable
private fun CategoryList(
    budgetId: Int,
    categoryList: List<BudgetCategory>,
    onAddCategoryPaymentClick: (BudgetCategory) -> Unit,
    onCategoryClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalAnimatedVisibilityScope.current

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
    )  {
        items(categoryList) {
            com.bungalow.budget.ui.composable.BudgetCategory(
                category = it,
                animatedVisibilityScope = visibilityScope,
                sharedTransitionScope = sharedScope,
                onActionButtonClick = onAddCategoryPaymentClick,
                onCategoryClick = { category -> onCategoryClick(budgetId, category.id) },
                actionButtonContent = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Payment",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    Content(
        budget = getMockedBudget(),
        onStartBudgetClick = {},
        onAddCategoryPaymentClick = {},
        onBudgetSettingsClick = {},
        onCategoryClick = { _, _ -> },
    )
}