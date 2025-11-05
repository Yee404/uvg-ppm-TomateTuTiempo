package com.example.tomatetutiempo.data.model

data class Tarea(
    val id: String = "",
    val nombre: String = "",
    val cursoId: String = "",
    val cursoNombre: String = "",
    val fecha: Long = 0L, // Timestamp en milisegundos
    val horasNecesarias: Int = 0,
    val descripcion: String = "",
    val completada: Boolean = false
)