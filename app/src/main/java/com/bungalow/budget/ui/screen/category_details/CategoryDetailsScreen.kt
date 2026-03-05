package com.bungalow.budget.ui.screen.category_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.R
import com.bungalow.budget.ui.composable.BudgetCategory
import com.bungalow.budget.ui.dialog.add_payment.AddPaymentDialog
import com.bungalow.budget.ui.dialog.add_payment.AddPaymentViewModel
import com.bungalow.budget.utils.getMockedCategories
import com.bungalow.budget.utils.toLocalDateTime
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment

@Composable
fun CategoryDetailsScreen(
    viewModel: CategoryDetailsViewModel = hiltViewModel(),
    addPaymentViewModel: AddPaymentViewModel = hiltViewModel(),
) {
    val category by viewModel.category.collectAsStateWithLifecycle()
    val showEditPaymentDialog by viewModel.showEditPaymentDialog.collectAsStateWithLifecycle()
    val showDeletePaymentDialog by viewModel.showDeletePaymentDialog.collectAsStateWithLifecycle()

    category?.let {
        Content(
            category = it,
            onEditPaymentClick = viewModel::onEditPaymentClick,
            onDeletePaymentClick = viewModel::onDeletePaymentClick
        )
    }

    LaunchedEffect(showEditPaymentDialog) {
        showEditPaymentDialog?.let { payment ->
            addPaymentViewModel.resetInputs(payment)
        }
    }

    showEditPaymentDialog?.let {
        category?.let { category ->
            AddPaymentDialog(
                viewModel = addPaymentViewModel,
                budgetCategory = category,
                onDismiss = viewModel::onEditPaymentDismissed,
                onConfirm = viewModel::onEditPaymentCompleted
            )
        }
    }

    showDeletePaymentDialog?.let {
        AlertDialog(
            onDismissRequest = viewModel::onDeletePaymentDismissed,
            title = {
                Text(text = stringResource(R.string.category_details_delete_payment_dialog_title))
            },
            text = {
                Text(text = stringResource(R.string.category_details_delete_payment_dialog_description))
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onDeletePaymentConfirmed(it) }
                ) {
                    Text(text = stringResource(R.string.general_apply))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::onDeletePaymentDismissed
                ) {
                    Text(text = stringResource(R.string.general_cancel))
                }
            }
        )
    }
}

@Composable
private fun Content(
    category: BudgetCategory,
    onEditPaymentClick: (CategoryPayment) -> Unit,
    onDeletePaymentClick: (CategoryPayment) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BudgetCategory(
            category = category,
            onAddCategoryPaymentClick = {},
            onCategoryClick = {}
        )
        PaymentsList(
            payments = category.payments,
            onEditPaymentClick = onEditPaymentClick,
            onDeletePaymentClick = onDeletePaymentClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PaymentsList(
    payments: List<CategoryPayment>,
    onEditPaymentClick: (CategoryPayment) -> Unit,
    onDeletePaymentClick: (CategoryPayment) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = payments,
            key = { it.id }
        ) { item ->
            Payment(
                payment = item,
                onEditPaymentClick = onEditPaymentClick,
                onDeletePaymentClick = onDeletePaymentClick,
            )
        }
    }
}

@Composable
private fun Payment(
    payment: CategoryPayment,
    onEditPaymentClick: (CategoryPayment) -> Unit,
    onDeletePaymentClick: (CategoryPayment) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        Text(
            text = payment.amount.toString(),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.width(70.dp)
        )

        Text(
            text = payment.note,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = payment.createdDateMillis.toLocalDateTime(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        IconButton(
            onClick = { onEditPaymentClick(payment) }
        ) {
            Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.general_edit))
        }

        IconButton(
            onClick = { onDeletePaymentClick(payment) }
        ) {
            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.general_delete))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    Content(
        category = getMockedCategories().first(),
        onEditPaymentClick = {},
        onDeletePaymentClick = {}
    )
}
