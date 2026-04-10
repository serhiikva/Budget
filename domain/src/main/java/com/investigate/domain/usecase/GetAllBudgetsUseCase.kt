package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.repository.BudgetRepository
import javax.inject.Inject

class GetAllBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(): List<Budget> {
        return budgetRepository.getAllBudgets()
    }
}