package com.bungalow.budget.ui.dialog.add_member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investigate.domain.model.BudgetCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMemberViewModel @Inject constructor(): ViewModel() {

    private val _email = MutableStateFlow<String?>(null)
    val email = _email.asStateFlow()

    private val _result = MutableSharedFlow<String?>()
    val result = _result.asSharedFlow()

    fun resetInputs() {
        _email.value = null
    }

    fun onNameChanged(name: String) {
        _email.value = name
    }

    fun onConfirmed() {
        viewModelScope.launch {
            _result.emit(_email.value)
        }
    }
}