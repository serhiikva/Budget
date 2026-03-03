package com.investigate.domain.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.investigate.domain.repository.BudgetRepository
import com.investigate.domain.repository.RemoteRepository
import timber.log.Timber

class SendToRemoteFirebaseWorker(
    private val budgetRepository: BudgetRepository,
    private val remoteRepository: RemoteRepository,
    workerParams: WorkerParameters,
    context: Context
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val budgetId = inputData.getInt(INPUT_BUDGET_ID_KEY, -1)
        return budgetRepository.getBudgetById(budgetId)?.let {
            try {
                remoteRepository.saveBudget(it)
                Result.success()
            } catch (e: Exception) {
                Timber.e("Failed to send budget to the remote Firebase, message: ${e.message}")
                Result.retry()
            }
        } ?: run {
            Timber.d("Failed to send budget to the remote Firebase, budget with id $budgetId does not exist")
            Result.failure()
        }
    }

    companion object {
        private const val INPUT_BUDGET_ID_KEY = "inputBudgetIdKey"
        private const val TAG = "SendToRemoteFirebaseWorkTag"

        fun enqueue(budgetId: Int, context: Context) {
            val inputData = Data.Builder().apply {
                putInt(INPUT_BUDGET_ID_KEY, budgetId)
            }.build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<SendToRemoteFirebaseWorker>()
                .addTag(TAG)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).apply {
                cancelAllWorkByTag(TAG)
                enqueue(workRequest)
            }
        }
    }
}