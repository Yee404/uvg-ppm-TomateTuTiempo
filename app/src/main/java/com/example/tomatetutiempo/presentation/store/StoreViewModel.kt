package com.example.tomatetutiempo.presentation.store

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StoreViewModel : ViewModel() {

    // estado interno que se puede cambiar
    private val _state = mutableStateOf(
        StoreState(
            productos = emptyList(),
            cargando = true
        )
    )

    // estado público solo lectura
    val state: State<StoreState> = _state

    init {
        cargarProductosFalsos()
    }

    private fun cargarProductosFalsos() {
        viewModelScope.launch {
            // Simular “cargando…”
            delay(1000)

            val listaFalsa = listOf(
                Producto(
                    id = 1,
                    nombre = "Pomodoro Pro",
                    descripcion = "Temporizador premium para estudio.",
                    precio = "Q29.99"
                ),
                Producto(
                    id = 2,
                    nombre = "Paquete de stickers",
                    descripcion = "Stickers motivacionales digitales.",
                    precio = "Q9.99"
                )
            )

            _state.value = StoreState(
                productos = listaFalsa,
                cargando = false
            )
        }
    }
}
