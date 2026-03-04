package com.bungalow.budget.ui.screen.start_budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.bungalow.budget.ui.dialog.add_category.AddCategoryDialog
import com.bungalow.budget.ui.dialog.add_category.AddCategoryViewModel
import com.bungalow.budget.ui.dialog.add_member.AddMemberDialog
import com.bungalow.budget.ui.dialog.add_member.AddMemberViewModel
import com.bungalow.budget.ui.theme.small
import com.bungalow.budget.utils.getMockedBudget
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory

@Composable
fun StartBudgetScreen(
    viewModel: StartBudgetViewModel = hiltViewModel(),
    addCategoryViewModel: AddCategoryViewModel = hiltViewModel(),
    addMemberViewModel: AddMemberViewModel = hiltViewModel()
) {
    val budget by viewModel.budget.collectAsStateWithLifecycle()
    val showAddCategoryDialog by viewModel.showAddCategoryDialog.collectAsStateWithLifecycle()
    val showAddMemberDialog by viewModel.showAddMemberDialog.collectAsStateWithLifecycle()
    val showConfirmFinishBudgetDialog by viewModel.showConfirmFinishBudgetDialog.collectAsStateWithLifecycle()

    Content(
        budget = budget,
        onAddCategoryClick = viewModel::onAddCategoryClick,
        onAddMemberClick = viewModel::onAddMemberClick,
        onFinishBudgetClick = viewModel::onFinishBudgetClick,
    )

    LaunchedEffect(showAddCategoryDialog) {
        if (showAddCategoryDialog) addCategoryViewModel.resetInputs()
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            viewModel = addCategoryViewModel,
            onDismiss = viewModel::onAddCategoryDismissed,
            onConfirm = viewModel::onAddCategoryConfirmed
        )
    }

    LaunchedEffect(showAddMemberDialog) {
        if (showAddMemberDialog) addMemberViewModel.resetInputs()
    }

    if (showAddMemberDialog) {
        AddMemberDialog(
            viewModel = addMemberViewModel,
            onDismiss = viewModel::onAddMemberDismissed,
            onConfirm = viewModel::onAddMemberConfirmed
        )
    }

    if (showConfirmFinishBudgetDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onFinishBudgetDismissed,
            title = {
                Text(text = stringResource(R.string.start_budget_finish_budget_dialog_title))
            },
            text = {
                Text(text = stringResource(R.string.start_budget_finish_budget_dialog_description))
            },
            confirmButton = {
                TextButton(
                    onClick = viewModel::onFinishBudgetConfirmed
                ) {
                    Text(text = stringResource(R.string.general_apply))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::onFinishBudgetDismissed
                ) {
                    Text(text = stringResource(R.string.general_cancel))
                }
            }
        )
    }
}

@Composable
private fun Content(
    budget: Budget,
    onAddCategoryClick: () -> Unit,
    onAddMemberClick: () -> Unit,
    onFinishBudgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Categories(
            categories = budget.categories,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(small))
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = onAddMemberClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(R.string.start_budget_add_member))
            }
            Button(
                onClick = onAddCategoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(R.string.start_budget_add_category))
            }
            Button(
                onClick = onFinishBudgetClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(R.string.start_budget_finish))
            }
        }
    }
}

@Composable
private fun Categories(
    categories: List<BudgetCategory>,
    modifier: Modifier = Modifier,
) {
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
    )  {
        items(categories) {
            com.bungalow.budget.ui.composable.BudgetCategory(
                category = it,
                onAddCategoryPaymentClick = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 540,
    heightDp = 1170
)
@Composable
private fun StartBudgetScreenPreview() {
    Content(
        budget = getMockedBudget(),
        onAddCategoryClick = {},
        onAddMemberClick = {},
        onFinishBudgetClick = {},
    )
}