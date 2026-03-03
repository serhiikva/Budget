package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.repository.BudgetRepository
import javax.inject.Inject

class CreateBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,

) {
    suspend operator fun invoke(budget: Budget) {
        budgetRepository.createBudget(budget)
    }
}