package com.example.tomatetutiempo.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class Tarea(
    val id: String = "",
    val nombre: String = "",
    val cursoId: String = "",
    val cursoNombre: String = "",
    val fecha: Timestamp? = null,
    val horasNecesarias: Int = 0,
    val descripcion: String = "",
    val completada: Boolean = false
)