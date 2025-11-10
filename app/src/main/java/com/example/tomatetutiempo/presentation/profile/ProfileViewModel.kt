package com.example.tomatetutiempo.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.repository.UserStatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            // Observar los cambios en las estadísticas del usuario
            UserStatsRepository.userStats.collect { stats ->
                _state.value = ProfileState(
                    isLoading = false,
                    name = stats.name,
                    email = stats.email,
                    gems = stats.gems,
                    completedTasks = stats.completedTasks,
                    streak = stats.streak
                )
            }
        }
    }
}