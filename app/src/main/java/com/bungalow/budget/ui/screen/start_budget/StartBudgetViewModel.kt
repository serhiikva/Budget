package com.bungalow.budget.ui.screen.start_budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bungalow.budget.ui.navigation.NavArg
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.usecase.CreateBudgetUseCase
import com.investigate.domain.usecase.GetUserEmailUseCase
import com.investigate.domain.usecase.ObserveActiveBudgetUseCase
import com.investigate.domain.usecase.ObserveBudgetByIdUseCase
import com.investigate.domain.usecase.UpdateBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartBudgetViewModel @Inject constructor(
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val observeActiveBudgetUseCase: ObserveActiveBudgetUseCase,
    private val observeBudgetByIdUseCase: ObserveBudgetByIdUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val budgetId: Int = savedStateHandle[NavArg.BUDGET_ID] ?: -1

    private val _budget = MutableStateFlow(Budget.empty())
    val budget: StateFlow<Budget> = _budget

    private val _showAddCategoryDialog = MutableStateFlow(false)
    val showAddCategoryDialog: StateFlow<Boolean> = _showAddCategoryDialog

    private val _showAddMemberDialog = MutableStateFlow(false)
    val showAddMemberDialog: StateFlow<Boolean> = _showAddMemberDialog

    init {
        viewModelScope.launch {
            if (budgetId == -1) {
                observeActiveBudgetUseCase.invoke()
            } else {
                observeBudgetByIdUseCase.invoke(budgetId)
            }.collect {
                _budget.value = it ?: Budget.empty()
            }
        }
    }

    fun onAddCategoryClick() {
        _showAddCategoryDialog.value = true
    }

    fun onAddCategoryConfirmed(category: BudgetCategory) {
        viewModelScope.launch {
            val userEmail = getUserEmailUseCase()

            userEmail?.let { user ->
                val updatedBudget = _budget.value.copy(
                    creatorEmail = user.email,
                    categories = _budget.value.categories + category,
                    startDateMillis = System.currentTimeMillis(),
                    isActive = true,
                    lastModified = System.currentTimeMillis()
                )

                createBudgetUseCase(updatedBudget)
                _budget.value = updatedBudget
                _showAddCategoryDialog.value = false
            }
        }
    }

    fun onAddCategoryDismissed() {
        _showAddCategoryDialog.value = false
    }

    fun onAddMemberClick() {
        _showAddMemberDialog.value = true
    }

    fun onAddMemberConfirmed(email: String) {
        viewModelScope.launch {
            val updatedBudget = _budget.value.copy(
                sharedWithEmails = _budget.value.sharedWithEmails + email
            )
            updateBudgetUseCase(updatedBudget)
            _budget.value = updatedBudget
            _showAddMemberDialog.value = false
        }
    }

    fun onAddMemberDismissed() {
        _showAddMemberDialog.value = false
    }

}