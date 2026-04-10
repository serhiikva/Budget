package com.investigate.domain.usecase

import com.investigate.domain.extensions.matches
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.repository.BudgetRepository
import timber.log.Timber
import javax.inject.Inject

class SearchMatchesUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(
        budgetId: Int,
        targetSearch: String
    ): List<BudgetCategory> {
        val budget = budgetRepository.getBudgetById(budgetId)
            ?: run {
                Timber.w("Failed to search payments, Budget with id=$budgetId not found")
                return emptyList()
            }

        return budget.categories
            .asSequence()
            .mapNotNull { category ->
                val filteredPayments = category.payments.filter { it.matches(targetSearch) }
                when {
                    filteredPayments.isNotEmpty() -> category.copy(payments = filteredPayments)
                    category.name.contains(targetSearch, true) -> category.copy(payments = emptyList())
                    else -> null
                }
            }
            .toList()
    }
}