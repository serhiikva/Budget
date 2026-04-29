package com.investigate.remotefirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.investigate.remotefirebase.model.BudgetDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseBudgetDataSource @Inject constructor(
    private val database: FirebaseDatabase,
    private val config: FirebaseConfig
) {

    fun observeUserBudgetIds(emailBase64: String): Flow<List<String>> = callbackFlow {
        val ref = database.getReference("${config.usersRef}/${emailBase64}")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ids = snapshot.children.mapNotNull { it.key }
                trySend(ids)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun observeBudget(id: String): Flow<BudgetDto?> = callbackFlow {
        val ref = database.getReference("${config.budgetsRef}/$id")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(BudgetDto::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun saveBudget(dto: BudgetDto) {
        val budgetId = dto.id.toString()
        database.getReference("${config.budgetsRef}/$budgetId").setValue(dto)

        database.getReference("${config.usersRef}/${dto.creatorEmail}/$budgetId")
            .setValue(true)

        dto.sharedWithEmails.keys.forEach { email ->
            database.getReference("${config.usersRef}/$email/$budgetId")
                .setValue(true)
        }
    }

    suspend fun getBudget(budgetId: Int): BudgetDto? {
        return try {
            val result = database
                .getReference("${config.budgetsRef}/$budgetId")
                .get()
                .await()

            return if (result.exists()) {
                result.getValue(BudgetDto::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}