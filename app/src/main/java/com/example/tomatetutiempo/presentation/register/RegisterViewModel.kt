package com.example.tomatetutiempo.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)

class RegisterViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)

            val result = repository.registerUser(name, email, password)

            if (result != null && result.user != null) {
                _uiState.value = RegisterUiState(registerSuccess = true)
            } else {
                _uiState.value = RegisterUiState(
                    error = "Error al crear la cuenta. El email podría estar en uso."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = RegisterUiState(error = null)
    }
}