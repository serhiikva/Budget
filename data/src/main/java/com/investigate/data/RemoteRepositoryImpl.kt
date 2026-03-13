package com.investigate.data

import com.investigate.data.mapper.toDto
import com.investigate.data.mapper.toModel
import com.investigate.data.utils.toBase64
import com.investigate.domain.model.Budget
import com.investigate.domain.repository.RemoteRepository
import com.investigate.remotefirebase.FirebaseBudgetDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.collections.emptyList

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSource: FirebaseBudgetDataSource
): RemoteRepository {
    override fun observeBudgets(email: String): Flow<List<Budget>> {
        return firebaseRemoteDataSource.observeUserBudgetIds(email.toBase64())
            .flatMapLatest { ids ->
                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        ids.map { id -> firebaseRemoteDataSource.observeBudget(id) }
                    ) { budgets ->
                        budgets
                            .filterNotNull()
                            .map { it.toModel() }
                    }
                }
            }
    }

    override suspend fun saveBudget(budget: Budget) {
        firebaseRemoteDataSource.saveBudget(budget.toDto())
    }

    override suspend fun getBudgetById(budgetId: Int): Budget? {
        return firebaseRemoteDataSource.getBudget(budgetId)?.toModel()
    }

    override suspend fun getBudgetLastModifiedMillis(budgetId: Int): Long? {
        return getBudgetById(budgetId)?.lastModified
    }
}