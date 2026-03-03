package com.investigate.domain.usecase

import com.investigate.domain.model.User
import com.investigate.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): User? {
        return authRepository.getUser()
    }
}