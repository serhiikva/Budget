package com.investigate.domain.repository

import com.investigate.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    suspend fun createBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun upsertBudget(budget: Budget)
    suspend fun getBudgetById(id: Int): Budget?
    suspend fun getActiveBudget(): Budget?
    suspend fun observeActiveBudget(): Flow<Budget?>
    suspend fun observeBudgetById(id: Int): Flow<Budget?>
    suspend fun getAllBudgets(): List<Budget>
}