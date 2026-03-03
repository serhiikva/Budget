package com.investigate.remotefirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.investigate.remotefirebase.model.BudgetDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseBudgetDataSource @Inject constructor(
    private val database: FirebaseDatabase
) {

    fun observeUserBudgetIds(emailBase64: String): Flow<List<String>> = callbackFlow {
        val ref = database.getReference("$USERS/${emailBase64}")

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
        val ref = database.getReference("$BUDGETS/$id")

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

    suspend fun saveBudget(dto: BudgetDto) {
        val budgetId = dto.id.toString()
        database.getReference("$BUDGETS/$budgetId").setValue(dto)

        database.getReference("$USERS/${dto.creatorEmail}/$budgetId")
            .setValue(true)

        dto.sharedWithEmails.keys.forEach { email ->
            database.getReference("$USERS/$email/$budgetId")
                .setValue(true)
        }
    }

    companion object {
        private const val BUDGETS = "budgets"
        private const val USERS = "users"
    }
}