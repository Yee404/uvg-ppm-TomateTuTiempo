package com.example.tomatetutiempo.presentation.store

data class StoreItem(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val costoGemas: Int,
    val comprado: Boolean = false
)

data class StoreState(
    val gemasUsuario: Int = 0,
    val items: List<StoreItem> = emptyList(),
    val mensaje: String? = null
)
