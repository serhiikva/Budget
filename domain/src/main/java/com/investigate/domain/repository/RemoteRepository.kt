package com.investigate.domain.repository

import com.investigate.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    fun observeBudgets(email: String): Flow<List<Budget>>
    suspend fun saveBudget(budget: Budget)
    suspend fun getBudgetLastModifiedMillis(budgetId: Int): Long?
    suspend fun getBudgetById(budgetId: Int): Budget?
}