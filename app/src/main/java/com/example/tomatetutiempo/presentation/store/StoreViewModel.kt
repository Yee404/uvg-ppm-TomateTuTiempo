package com.example.tomatetutiempo.presentation.store

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StoreViewModel : ViewModel() {

    private val _state = MutableStateFlow(StoreState())
    val state: StateFlow<StoreState> = _state.asStateFlow()

    init {
        cargarTienda()
    }

    private fun cargarTienda() {
        val gemasIniciales = 80

        val listaItems = listOf(
            StoreItem(
                id = "tema_pastel",
                nombre = "Tema pastel",
                descripcion = "Colores suaves para estudiar más relajado.",
                costoGemas = 30
            ),
            StoreItem(
                id = "tema_oscuro",
                nombre = "Tema oscuro",
                descripcion = "Ideal para estudiar de noche.",
                costoGemas = 40
            ),
            StoreItem(
                id = "sonidos",
                nombre = "Paquete de sonidos",
                descripcion = "Sonidos de lluvia y bosque para concentrarte.",
                costoGemas = 25
            ),
            StoreItem(
                id = "stickers",
                nombre = "Stickers motivacionales",
                descripcion = "Frases lindas cuando completas tareas.",
                costoGemas = 20
            )
        )

        _state.value = StoreState(
            gemasUsuario = gemasIniciales,
            items = listaItems
        )
    }

    fun comprar(itemId: String) {
        val estadoActual = _state.value
        val item = estadoActual.items.find { it.id == itemId } ?: return

        if (item.comprado) {
            _state.update { it.copy(mensaje = "Ya compraste este artículo.") }
            return
        }

        if (estadoActual.gemasUsuario < item.costoGemas) {
            _state.update { it.copy(mensaje = "No tienes suficientes gemas.") }
            return
        }

        _state.update {
            it.copy(
                gemasUsuario = it.gemasUsuario - item.costoGemas,
                items = it.items.map { articulo ->
                    if (articulo.id == itemId) articulo.copy(comprado = true) else articulo
                },
                mensaje = "Compra realizada con éxito."
            )
        }
    }

    fun limpiarMensaje() {
        _state.update { it.copy(mensaje = null) }
    }
}
