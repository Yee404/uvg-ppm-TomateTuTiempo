package com.example.tomatetutiempo.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    // StateFlow para manejar el estado
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadFakeData()
    }

    private fun loadFakeData() {
        viewModelScope.launch {
            delay(1500) // Simula carga
            _state.value = ProfileState(
                isLoading = false,
                name = "Usuario Prueba",
                email = "test@uvg.edu.gt"
            )
        }
    }
}

