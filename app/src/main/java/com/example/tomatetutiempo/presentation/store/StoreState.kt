package com.example.tomatetutiempo.presentation.store

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: String
)

data class StoreState(
    val productos: List<Producto> = emptyList(),
    val cargando: Boolean = false

)
