package com.investigate.domain.usecase

import android.content.Context
import com.investigate.domain.model.User
import com.investigate.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(activityContext: Context): Result<User?> {
        return authRepository.signInWithGoogle(activityContext)
    }
}