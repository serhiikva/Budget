package com.bungalow.budget.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.R
import com.bungalow.budget.ui.composable.BudgetCategory
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
    onMenuClick: () -> Unit,
    onPaymentsClick: () -> Unit
) {
    val budget by viewModel.budget.collectAsStateWithLifecycle()
    val categoryToAddPayment by viewModel.categoryToAddPayment.collectAsStateWithLifecycle(null)
    val search by viewModel.search.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    Content(
        budget = budget,
        search = search ?: "",
        searchResult = searchResult ?: emptyList(),
        onSearch = viewModel::onSearch,
        onSearchFinished = viewModel::onSearchFinished,
        onStartBudgetClick = onStartBudgetClick,
        onAddCategoryPaymentClick = viewModel::onAddCategoryPaymentClick,
        onBudgetSettingsClick = onBudgetSettingsClick,
        onCategoryClick = onCategoryClick,
        onMenuClick = onMenuClick,
        onPaymentsClick = onPaymentsClick
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
    search: String,
    searchResult: List<BudgetCategory>,
    onSearch: (String) -> Unit,
    onSearchFinished: () -> Unit,
    onStartBudgetClick: () -> Unit,
    onMenuClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onAddCategoryPaymentClick: (BudgetCategory) -> Unit,
    onBudgetSettingsClick: (Int) -> Unit,
    onCategoryClick: (Int, String) -> Unit
) {
    if (budget.id != 0) {
        BudgetDetails(
            budget = budget,
            search = search,
            searchResult = searchResult,
            onSearch = onSearch,
            onSearchFinished = onSearchFinished,
            onAddCategoryPaymentClick = onAddCategoryPaymentClick,
            onBudgetSettingsClick = onBudgetSettingsClick,
            onCategoryClick = onCategoryClick,
            onMenuClick = onMenuClick,
            onPaymentsClick = onPaymentsClick
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
    search: String,
    searchResult: List<BudgetCategory>,
    onSearch: (String) -> Unit,
    onSearchFinished: () -> Unit,
    onAddCategoryPaymentClick: (BudgetCategory) -> Unit,
    onBudgetSettingsClick: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onCategoryClick: (Int, String) -> Unit
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    val collapseSearch = {
        onSearchFinished()
        isSearchExpanded = false
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            onPaymentsClick = onPaymentsClick,
            onMenuClick = onMenuClick,
            onSearchClick = {
                if (isSearchExpanded) collapseSearch()
                else isSearchExpanded = true
            }
        )

        AnimatedVisibility(
            visible = isSearchExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Search(
                search = search,
                onSearch = onSearch,
                onSearchFinished = collapseSearch,
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
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

            if (isSearchExpanded) {
                SearchResults(searchResult = searchResult)
            }
        }
    }
}

@Composable
private fun Search(
    search: String,
    onSearch: (String) -> Unit,
    onSearchFinished: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = onSearch,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        IconButton(onClick = onSearchFinished) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close search"
            )
        }
    }
}

@Composable
private fun SearchResults(searchResult: List<BudgetCategory>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        items(searchResult) {
            Text(
                text = "Category: ${it.name}",
                modifier = Modifier.padding(start = 8.dp)
            )
            if (it.payments.isNotEmpty()) {
                Text(
                    text = "Payments:",
                    modifier = Modifier.padding(start = 12.dp)
                )
                it.payments.forEach {
                    Text(
                        text = "${it.amount}, ${it.note}",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
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
    onMenuClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "menu"
            )
        }
        Row {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
            IconButton(onClick = onPaymentsClick) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "All Payments"
                )
            }
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

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        items(categoryList) {
            BudgetCategory(
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
        search = "Test search",
        searchResult = emptyList(),
        onSearch = {},
        onSearchFinished = {},
        onStartBudgetClick = {},
        onAddCategoryPaymentClick = {},
        onBudgetSettingsClick = {},
        onCategoryClick = { _, _ -> },
        onMenuClick = {},
        onPaymentsClick = {},
    )
}