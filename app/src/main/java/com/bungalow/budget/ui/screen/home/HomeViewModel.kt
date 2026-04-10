@file:OptIn(FlowPreview::class)

package com.bungalow.budget.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bungalow.budget.utils.launchOnIoDispatcher
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.usecase.AddCategoryPaymentUseCase
import com.investigate.domain.usecase.ObserveActiveBudgetUseCase
import com.investigate.domain.usecase.SearchMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeActiveBudgetUseCase: ObserveActiveBudgetUseCase,
    private val addCategoryPaymentUseCase: AddCategoryPaymentUseCase,
    private val searchMatchesUseCase: SearchMatchesUseCase
): ViewModel() {
    private val _budget = MutableStateFlow(Budget.empty())
    val budget: StateFlow<Budget> = _budget

    private val _categoryToAddPayment = MutableStateFlow<BudgetCategory?>(null)
    val categoryToAddPayment = _categoryToAddPayment.asStateFlow()

    private val _search = MutableStateFlow<String?>(null)
    val search = _search.asStateFlow()

    private val _searchResult = MutableStateFlow<List<BudgetCategory>?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _searchLoading = MutableStateFlow<Boolean?>(null)
    val searchLoading = _searchLoading.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launchOnIoDispatcher {
            observeActiveBudgetUseCase.invoke()
                .filterNotNull()
                .distinctUntilChanged()
                .collect {
                    _budget.value = it
                }
        }
    }

    fun onAddCategoryPaymentClick(category: BudgetCategory) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun onSearch(search: String) {
        _search.value = search

        searchJob?.cancel()
        if (search.isBlank()) {
            _searchResult.value = emptyList()
            _searchLoading.value = false
        } else {
            searchJob = viewModelScope.launchOnIoDispatcher {
                _searchLoading.value = true
                delay(SEARCH_DELAY)
                _searchResult.value = searchMatchesUseCase(
                    _budget.value.id,
                    search
                )
                _searchLoading.value = false
            }
        }
    }

    fun onSearchFinished() {
        _search.value = null
        _searchLoading.value = null
        _searchResult.value = null
    }

    companion object {
        private const val SEARCH_DELAY = 300L
    }
}