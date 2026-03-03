package com.bungalow.budget.ui.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investigate.domain.model.User
import com.investigate.domain.sync.SyncManager
import com.investigate.domain.usecase.GetUserEmailUseCase
import com.investigate.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val syncManager: SyncManager
): ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asSharedFlow()

    private val _loginErrorMessage = MutableSharedFlow<String>()
    val loginErrorMessage = _loginErrorMessage.asSharedFlow()

    init {
        getUserEmailUseCase()?.let {
            onUserLoggedIn(it)
        }
    }

    fun login(activityContext: Context) {
        viewModelScope.launch {
            loginUseCase(activityContext).fold(
                onSuccess = {
                    it?.let { onUserLoggedIn(it) }
                },
                onFailure =  { _loginErrorMessage.emit(it.message ?: "") }
            )
        }
    }

    private fun onUserLoggedIn(user: User) {
        Timber.d("User logged in")
        syncManager.startSync(user.email)
        _isLoggedIn.value = true
    }
}