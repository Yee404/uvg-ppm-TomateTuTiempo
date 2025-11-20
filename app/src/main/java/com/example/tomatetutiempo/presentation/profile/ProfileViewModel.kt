package com.example.tomatetutiempo.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.value = ProfileState(isLoading = true)
            val user = userRepository.getUserProfile()

            if (user != null) {
                _state.value = ProfileState(
                    isLoading = false,
                    name = user.name,
                    email = user.email,
                    gems = user.gems,
                    completedTasks = user.completedTasks,
                    streak = user.streak
                    )
            } else {
                _state.value = ProfileState(isLoading = false, name = "Error", email = "No se pudo cargar el perfil")
            }
        }
    }
}