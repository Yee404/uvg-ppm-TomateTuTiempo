package com.example.tomatetutiempo.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Simulamos un pequeño delay como si cargáramos datos
            delay(500)

            // DATOS HARDCODED - Después conectarás con Firebase
            _state.value = ProfileState(
                isLoading = false,
                name = "Sara Rodriguez",
                email = "sara.rodriguez@example.com",
                gems = 3500,
                completedTasks = 45,
                streak = 7
            )
        }
    }

    // Función para actualizar las estadísticas (la usarás después)
    fun updateStats(gems: Int? = null, completedTasks: Int? = null, streak: Int? = null) {
        _state.value = _state.value.copy(
            gems = gems ?: _state.value.gems,
            completedTasks = completedTasks ?: _state.value.completedTasks,
            streak = streak ?: _state.value.streak
        )
    }
}