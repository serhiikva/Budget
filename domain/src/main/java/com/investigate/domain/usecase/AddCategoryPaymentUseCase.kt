package com.investigate.domain.usecase

import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.repository.BudgetRepository
import timber.log.Timber
import javax.inject.Inject

class AddCategoryPaymentUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(
        budgetId: Int,
        payment: CategoryPayment
    ) {
        if (payment.amount <= 0f) {
            Timber.w("Failed to add payment, incorrect amount: $payment")
            return
        }

        val budget = budgetRepository.getBudgetById(budgetId)
            ?: run {
                Timber.w("Failed to add payment, Budget with id=$budgetId not found")
                return
            }

        var categoryFound = false

        val updatedCategories = budget.categories.map { category ->
            if (category.id == payment.categoryId) {
                categoryFound = true

                category.copy(
                    payments = category.payments + payment
                )
            } else {
                category
            }
        }

        if (!categoryFound) {
            Timber.w("Failed to add payment, category with id=${payment.categoryId} not found")
            return
        }

        val updatedBudget = budget.copy(categories = updatedCategories, lastModified = System.currentTimeMillis())

        budgetRepository.updateBudget(updatedBudget)

        Timber.d("Payment added: $payment")
    }
}