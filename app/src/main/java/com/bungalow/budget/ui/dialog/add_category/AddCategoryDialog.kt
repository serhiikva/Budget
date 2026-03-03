package com.bungalow.budget.ui.dialog.add_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.R
import com.bungalow.budget.ui.theme.extraMedium
import com.bungalow.budget.ui.theme.medium
import com.investigate.domain.model.BudgetCategory

@Composable
fun AddCategoryDialog(
    viewModel: AddCategoryViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onConfirm: (BudgetCategory) -> Unit
) {

    val name = viewModel.name.collectAsStateWithLifecycle().value
    val amount = viewModel.amount.collectAsStateWithLifecycle().value
    val result = viewModel.result.collectAsStateWithLifecycle(null).value

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(extraMedium),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(extraMedium)
            ) {

                Text(
                    text = stringResource(R.string.dialog_add_category_title),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(medium))

                OutlinedTextField(
                    value = name ?: "",
                    onValueChange = { viewModel.onNameChanged(it) },
                    label = { Text(stringResource(R.string.general_name)) }
                )

                Spacer(Modifier.height(medium))

                OutlinedTextField(
                    value = amount ?: "",
                    onValueChange = { viewModel.onAmountChanged(it) },
                    label = { Text(stringResource(R.string.general_amount)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(Modifier.height(extraMedium))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.general_cancel))
                    }
                    Button(
                        onClick = { viewModel.onConfirmed() },
                        enabled = name != null && amount != null
                    ) {
                        Text(stringResource(R.string.general_apply))
                    }
                }
            }
        }
    }

    result?.let {
        onConfirm(it)
    }

}