package com.investigate.domain.usecase

import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.repository.BudgetRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
) {
    suspend operator fun invoke(budgetId: Int, categoryId: String): BudgetCategory? {
        return budgetRepository.getBudgetById(budgetId)?.categories?.first { it.id == categoryId }
    }
}