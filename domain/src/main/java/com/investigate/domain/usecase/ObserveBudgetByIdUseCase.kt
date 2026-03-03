package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBudgetByIdUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(id: Int): Flow<Budget?> {
        return budgetRepository.observeActiveBudget()
    }
}