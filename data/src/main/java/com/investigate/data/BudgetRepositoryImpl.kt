package com.investigate.data

import com.investigate.data.mapper.toEntity
import com.investigate.data.mapper.toModel
import com.investigate.database.dao.BudgetDao
import com.investigate.domain.model.Budget
import com.investigate.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
): BudgetRepository {
    override suspend fun createBudget(budget: Budget) {
        budgetDao.insert(budget.toEntity())
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetDao.update(budget.toEntity())
    }

    override suspend fun upsertBudget(budget: Budget) {
        budgetDao.upsert(budget.toEntity())
    }

    override suspend fun getBudgetById(id: Int): Budget? {
        return budgetDao.getById(id)?.toModel()
    }

    override suspend fun getActiveBudget(): Budget? {
        return budgetDao.getActive()?.toModel()
    }

    override suspend fun observeActiveBudget(): Flow<Budget?> {
        return budgetDao.getActiveObservable().map { it?.toModel() }
    }

    override suspend fun observeBudgetById(id: Int): Flow<Budget?> {
        return budgetDao.getByIdObservable(id).map { it?.toModel() }
    }

    override suspend fun getAllBudgets(): List<Budget> {
        return budgetDao.getAll().map { it.toModel() }
    }

}