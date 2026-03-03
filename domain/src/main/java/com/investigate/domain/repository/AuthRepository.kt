package com.investigate.domain.repository

import android.content.Context
import com.investigate.domain.model.User

interface AuthRepository {
    suspend fun signInWithGoogle(activityContext: Context): Result<User?>
    suspend fun signOut(activityContext: Context)
    fun getUser(): User?
}