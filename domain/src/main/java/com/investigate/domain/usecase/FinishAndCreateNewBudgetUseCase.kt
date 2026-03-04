package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.repository.BudgetRepository
import com.investigate.domain.repository.RemoteRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FinishAndCreateNewBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val remoteRepository: RemoteRepository

) {
    suspend operator fun invoke(budgetIdToFinish: Int, newBudget: Budget) {
        budgetRepository.getBudgetById(budgetIdToFinish)?.let {
            val budgetToFinish = it.copy(
                isActive = false,
                endDateMillis = System.currentTimeMillis()
            )
            budgetRepository.updateBudget(budgetToFinish)
            remoteRepository.saveBudget(budgetToFinish)
        }
        budgetRepository.createBudget(newBudget)
    }
}