package com.example.tomatetutiempo.presentation.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(StoreState())
    val state: StateFlow<StoreState> = _state.asStateFlow()

    private val allStoreItems = listOf(
        StoreItem("tema_pastel", "Tema pastel", "Colores suaves para estudiar.", 30),
        StoreItem("tema_oscuro", "Tema oscuro", "Ideal para estudiar de noche.", 40),
        StoreItem("sonidos", "Paquete de sonidos", "Sonidos de lluvia y bosque.", 25),
        StoreItem("stickers", "Stickers motivacionales", "Frases lindas al completar tareas.", 20)
    )

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val user = userRepository.getUserProfile()
            if (user != null) {
                _state.update {
                    it.copy(
                        gemasUsuario = user.gems,
                        items = allStoreItems.map { item ->
                            item.copy(comprado = user.purchasedItems.contains(item.id))
                        }
                    )
                }
            }
        }
    }

    fun comprar(itemId: String) {
        viewModelScope.launch {
            val item = _state.value.items.find { it.id == itemId } ?: return@launch
            val userGems = _state.value.gemasUsuario

            if (userGems >= item.costoGemas && !item.comprado) {
                userRepository.purchaseItem(itemId, item.costoGemas)

                loadUserData()
                _state.update { it.copy(mensaje = "¡Compra realizada!") }

            } else if (item.comprado) {
                _state.update { it.copy(mensaje = "Ya tienes este artículo.") }
            } else {
                _state.update { it.copy(mensaje = "No tienes suficientes gemas.") }
            }
        }
    }

    fun limpiarMensaje() {
        _state.update { it.copy(mensaje = null) }
    }
}