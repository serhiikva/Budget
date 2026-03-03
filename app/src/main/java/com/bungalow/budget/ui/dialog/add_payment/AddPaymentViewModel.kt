package com.bungalow.budget.ui.dialog.add_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investigate.domain.model.CategoryPayment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(): ViewModel() {

    private val _note = MutableStateFlow<String?>(null)
    val note: StateFlow<String?> = _note

    private val _amount = MutableStateFlow<String?>(null)
    val amount: StateFlow<String?> = _amount

    private val _result = MutableSharedFlow<CategoryPayment?>()
    val result = _result.asSharedFlow()

    private var categoryId: String = ""

    fun onNoteChanged(name: String) {
        _note.value = name
    }

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    fun onConfirmed() {
        viewModelScope.launch {
            _result.emit(
                CategoryPayment(
                    note = _note.value ?: "",
                    categoryId = categoryId,
                    amount = _amount.value?.toIntOrNull() ?: 0,
                    createdDateMillis = System.currentTimeMillis()
                )
            )
        }
    }

    fun resetInputs(categoryId: String) {
        _note.value = null
        _amount.value = null
        this.categoryId = categoryId
    }
}