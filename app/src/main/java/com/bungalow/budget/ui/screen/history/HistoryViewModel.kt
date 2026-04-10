package com.bungalow.budget.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investigate.domain.model.Budget
import com.investigate.domain.usecase.GetAllBudgetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllBudgetsUseCase: GetAllBudgetsUseCase
): ViewModel() {
    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets = _budgets.asStateFlow()

    init {
        viewModelScope.launch {
            _budgets.value = getAllBudgetsUseCase()
        }
    }
}