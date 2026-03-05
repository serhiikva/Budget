package com.investigate.domain.usecase

import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.repository.BudgetRepository
import javax.inject.Inject

class UpdateCategoryPaymentUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val updateBudgetUseCase: UpdateBudgetUseCase
) {
    suspend operator fun invoke(budgetId: Int, categoryPayment: CategoryPayment) {
        val budget = budgetRepository.getBudgetById(budgetId) ?: return

        val categoryId = categoryPayment.categoryId

        val updatedCategories = budget.categories.map { category ->
            if (category.id != categoryId) return@map category

            val updatedPayments = category.payments.map { payment ->
                if (payment.id == categoryPayment.id) categoryPayment else payment
            }

            category.copy(payments = updatedPayments)
        }

        val updatedBudget = budget.copy(
            categories = updatedCategories,
            lastModified = System.currentTimeMillis()
        )

        updateBudgetUseCase(updatedBudget)
    }
}