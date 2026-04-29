package com.bungalow.budget.ui.screen.payments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.R
import com.bungalow.budget.ui.composable.NavigationToolbar
import com.bungalow.budget.ui.model.PaymentItemUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentsScreen(
    viewModel: PaymentsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val payments by viewModel.payments.collectAsStateWithLifecycle()

    Content(
        payments = payments,
        onBackClick = onBackClick
    )
}

@Composable
private fun Content(
    payments: List<PaymentItemUi>,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NavigationToolbar(
            titleResId = R.string.payments_title,
            onBackClick = onBackClick
        )
        if (payments.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "No payments yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(payments, key = { it.id }) { payment ->
                    PaymentItem(payment = payment)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun PaymentItem(payment: PaymentItemUi) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = payment.categoryName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = payment.note.ifBlank { "—" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${payment.amount}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = dateFormatter.format(Date(payment.createdDateMillis)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentsPreview() {
    Content(
        payments = listOf(
            PaymentItemUi(
                id = "1",
                categoryName = "Food",
                amount = 1200,
                note = "Groceries",
                createdDateMillis = System.currentTimeMillis()
            ),
            PaymentItemUi(
                id = "2",
                categoryName = "Transport",
                amount = 350,
                note = "",
                createdDateMillis = System.currentTimeMillis() - 3_600_000
            )
        ),
        onBackClick = {}
    )
}
