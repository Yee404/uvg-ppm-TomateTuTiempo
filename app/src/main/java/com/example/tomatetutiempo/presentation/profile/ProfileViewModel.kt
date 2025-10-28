package com.example.tomatetutiempo.ui.presentation.profile // Asegúrate de que el package sea correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    // Usa el UserRepository (que ya no tiene KTX)
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Llama a la función del repositorio.
            // Esta función ya la modificamos para que no use KTX.
            val user = userRepository.getUserProfile()

            _state.value = if (user != null) {
                ProfileState(
                    isLoading = false,
                    name = user.name,
                    email = user.email
                )
            } else {
                ProfileState(isLoading = false, name = "Error al cargar", email = "Inicia sesión")
            }
        }
    }
}