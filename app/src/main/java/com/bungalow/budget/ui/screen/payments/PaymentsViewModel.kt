package com.bungalow.budget.ui.screen.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bungalow.budget.ui.model.PaymentItemUi
import com.investigate.domain.usecase.ObserveActiveBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val observeActiveBudgetUseCase: ObserveActiveBudgetUseCase
) : ViewModel() {

    private val _payments = MutableStateFlow<List<PaymentItemUi>>(emptyList())
    val payments = _payments.asStateFlow()

    init {
        viewModelScope.launch {
            observeActiveBudgetUseCase().collect { budget ->
                _payments.value = budget?.categories
                    ?.flatMap { category ->
                        category.payments.map { payment ->
                            PaymentItemUi(
                                id = payment.id,
                                categoryName = category.name,
                                amount = payment.amount,
                                note = payment.note,
                                createdDateMillis = payment.createdDateMillis
                            )
                        }
                    }
                    ?.sortedByDescending { it.createdDateMillis }
                    ?: emptyList()
            }
        }
    }
}
