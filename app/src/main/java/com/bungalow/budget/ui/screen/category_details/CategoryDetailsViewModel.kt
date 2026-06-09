package com.bungalow.budget.ui.screen.category_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bungalow.budget.ui.navigation.CategoryDetails
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.usecase.DeleteCategoryPaymentUseCase
import com.investigate.domain.usecase.GetCategoryUseCase
import com.investigate.domain.usecase.UpdateCategoryPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val updateCategoryPaymentUseCase: UpdateCategoryPaymentUseCase,
    private val deleteCategoryPaymentUseCase: DeleteCategoryPaymentUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val budgetId: Int = savedStateHandle.toRoute<CategoryDetails>().budgetId
    private val categoryId: String = savedStateHandle.toRoute<CategoryDetails>().categoryId

    private val _category = MutableStateFlow<BudgetCategory?>(null)
    val category = _category.asStateFlow()

    private val _showEditPaymentDialog = MutableStateFlow<CategoryPayment?>(null)
    val showEditPaymentDialog = _showEditPaymentDialog.asStateFlow()

    private val _showDeletePaymentDialog = MutableStateFlow<CategoryPayment?>(null)
    val showDeletePaymentDialog = _showDeletePaymentDialog.asStateFlow()

    init {
        getCategory()
    }

    fun onEditPaymentClick(payment: CategoryPayment) {
        _showEditPaymentDialog.value = payment
    }

    fun onEditPaymentCompleted(payment: CategoryPayment) {
        viewModelScope.launch {
            updateCategoryPaymentUseCase(budgetId, payment)
            getCategory()
            _showEditPaymentDialog.value = null
        }
    }

    fun onEditPaymentDismissed() {
        _showEditPaymentDialog.value = null
    }

    fun onDeletePaymentClick(payment: CategoryPayment) {
        _showDeletePaymentDialog.value = payment
    }

    fun onDeletePaymentConfirmed(payment: CategoryPayment) {
        viewModelScope.launch {
            deleteCategoryPaymentUseCase(budgetId, payment)
            getCategory()
            _showDeletePaymentDialog.value = null
        }
    }

    fun onDeletePaymentDismissed() {
        _showDeletePaymentDialog.value = null
    }

    private fun getCategory() {
        viewModelScope.launch {
            _category.value = getCategoryUseCase(budgetId, categoryId)
        }
    }
}