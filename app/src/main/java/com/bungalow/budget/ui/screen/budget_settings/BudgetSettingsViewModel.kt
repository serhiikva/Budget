package com.bungalow.budget.ui.screen.budget_settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bungalow.budget.ui.navigation.StartBudget
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.usecase.CreateBudgetUseCase
import com.investigate.domain.usecase.DeleteCategoryUseCase
import com.investigate.domain.usecase.FinishAndCreateNewBudgetUseCase
import com.investigate.domain.usecase.GetUserEmailUseCase
import com.investigate.domain.usecase.ObserveActiveBudgetUseCase
import com.investigate.domain.usecase.ObserveBudgetByIdUseCase
import com.investigate.domain.usecase.UpdateBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetSettingsViewModel @Inject constructor(
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val observeActiveBudgetUseCase: ObserveActiveBudgetUseCase,
    private val observeBudgetByIdUseCase: ObserveBudgetByIdUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val finishAndCreateNewBudgetUseCase: FinishAndCreateNewBudgetUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val budgetId: Int = savedStateHandle.toRoute<StartBudget>().budgetId ?: -1

    private val _budget = MutableStateFlow(Budget.empty())
    val budget = _budget.asStateFlow()

    private val _showAddCategoryDialog = MutableStateFlow(false)
    val showAddCategoryDialog = _showAddCategoryDialog.asStateFlow()

    private val _showDeleteCategoryDialog = MutableStateFlow("")
    val showDeleteCategoryDialog = _showDeleteCategoryDialog.asStateFlow()

    private val _showAddMemberDialog = MutableStateFlow(false)
    val showAddMemberDialog = _showAddMemberDialog.asStateFlow()

    private val _showConfirmFinishBudgetDialog = MutableStateFlow(false)
    val showConfirmFinishBudgetDialog = _showConfirmFinishBudgetDialog.asStateFlow()

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

    fun onDeleteCategoryClick(category: BudgetCategory) {
        _showDeleteCategoryDialog.value = category.id
    }

    fun onDeleteCategoryConfirmed(categoryId: String) {
        viewModelScope.launch {
            deleteCategoryUseCase(
                budgetId = budgetId,
                categoryId = categoryId
            )
            _showDeleteCategoryDialog.value = ""
        }
    }

    fun onDeleteCategoryDismissed() {
        _showDeleteCategoryDialog.value = ""
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

    fun onFinishBudgetClick() {
        _showConfirmFinishBudgetDialog.value = true
    }

    fun onFinishBudgetConfirmed() {
        viewModelScope.launch {
            val userEmail = getUserEmailUseCase()

            userEmail?.let { user ->
                val newBudget = _budget.value.copy(
                    id = 0,
                    creatorEmail = user.email,
                    categories = _budget.value.categories.map { BudgetCategory(
                        name = it.name,
                        budgetAmount = it.budgetAmount,
                        payments = emptyList()
                    ) },
                    startDateMillis = System.currentTimeMillis(),
                    isActive = true,
                    lastModified = System.currentTimeMillis()
                )

                finishAndCreateNewBudgetUseCase(_budget.value.id, newBudget)

                _showConfirmFinishBudgetDialog.value = false
            }
        }
    }

    fun onFinishBudgetDismissed() {
        _showConfirmFinishBudgetDialog.value = false
    }

}