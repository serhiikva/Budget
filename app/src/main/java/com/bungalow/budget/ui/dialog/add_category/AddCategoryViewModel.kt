package com.bungalow.budget.ui.dialog.add_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investigate.domain.model.BudgetCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(): ViewModel() {

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _amount = MutableStateFlow<String?>(null)
    val amount = _amount.asStateFlow()

    private val _result = MutableSharedFlow<BudgetCategory?>()
    val result = _result.asSharedFlow()

    fun resetInputs() {
        _name.value = null
        _amount.value = null
    }

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    fun onConfirmed() {
        viewModelScope.launch {
            _result.emit(
                BudgetCategory(
                    name = _name.value ?: "",
                    budgetAmount = _amount.value?.toIntOrNull() ?: 0,
                    payments = emptyList()
                )
            )
        }
    }
}