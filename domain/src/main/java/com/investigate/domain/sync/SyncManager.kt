package com.investigate.domain.sync

import com.investigate.domain.repository.BudgetRepository
import com.investigate.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class SyncManager @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val remoteRepository: RemoteRepository,
) {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isRunning = AtomicBoolean(false)

    fun startSync(email: String)  {
        if (isRunning.exchange(true)) return
        Timber.d("Sync started")

        syncScope.launch {
            budgetRepository.observeActiveBudget()
                .collect {
                    it?.let { activeBudget ->
                        val isRequiredUpdate = (remoteRepository
                            .getBudgetLastModifiedMillis(activeBudget.id)
                            ?: 0L) < activeBudget.lastModified

                        if (isRequiredUpdate) {
                            Timber.d("Active budget is changed, begin sync")
                            remoteRepository.saveBudget(it)
                        } else {
                            Timber.d("Nothing is changed, skip")
                        }
                    }
                }
        }

        syncScope.launch {
            remoteRepository.observeBudgets(email)
                .map {
                    it.filter { remote ->
                        budgetRepository.getBudgetById(remote.id)?.let { local ->
                            local.lastModified < remote.lastModified
                        } ?: true
                    }
                }
                .filter { it.isNotEmpty() }
                .collect { budgets ->
                    Timber.d("Changes from remote received, update database, $budgets")
                    budgets.forEach {
                        budgetRepository.upsertBudget(it)
                    }
                }
        }
    }

    fun stopSync() {
        syncScope.cancel("Sync stopped")
        isRunning.exchange(false)
        Timber.d("Sync stopped")
    }
}