package com.investigate.domain.usecase

import com.investigate.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val updateBudgetUseCase: UpdateBudgetUseCase
) {
    suspend operator fun invoke(budgetId: Int, categoryId: String) {
        val budget = budgetRepository.getBudgetById(budgetId) ?: return
        val updatedBudget = budget.copy(categories = budget.categories.filter { it.id != categoryId })
        updateBudgetUseCase(updatedBudget)
    }
}