package com.bungalow.budget.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.repository.BudgetRepository
import com.investigate.domain.usecase.AddCategoryPaymentUseCase
import com.investigate.domain.usecase.ObserveActiveBudgetUseCase
import com.investigate.domain.usecase.ObserveBudgetByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeActiveBudgetUseCase: ObserveActiveBudgetUseCase,
    private val observeBudgetByIdUseCase: ObserveBudgetByIdUseCase,
    private val addCategoryPaymentUseCase: AddCategoryPaymentUseCase,
    private val budgetRepository: BudgetRepository
): ViewModel() {
    private val _budget = MutableStateFlow(Budget.empty())
    val budget: StateFlow<Budget> = _budget

    private val _categoryToAddPayment = MutableStateFlow<BudgetCategory?>(null)
    val categoryToAddPayment = _categoryToAddPayment.asStateFlow()

    init {
        viewModelScope.launch {
            observeActiveBudgetUseCase.invoke()
                .filterNotNull()
                .distinctUntilChanged()
                .collect {
                    _budget.value = it
                }
        }
    }

    fun onAddCategoryPaymentClick(category: BudgetCategory) {
        viewModelScope.launch {
            _categoryToAddPayment.emit(category)
        }
    }

    fun onCategoryPaymentConfirmed(payment: CategoryPayment) {
        viewModelScope.launch {
            addCategoryPaymentUseCase.invoke(
                budgetId = _budget.value.id,
                payment = payment
            )
            _categoryToAddPayment.value = null
        }
    }

    fun onAddCategoryPaymentDismissed() {
        _categoryToAddPayment.value = null
    }

}