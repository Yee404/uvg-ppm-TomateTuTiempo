package com.example.tomatetutiempo.data.repository

import com.example.tomatetutiempo.data.model.Tarea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repositorio Singleton para manejar las tareas de la aplicación
 * Esto permite compartir las tareas entre diferentes pantallas
 */
object TaskRepository {

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    fun agregarTarea(tarea: Tarea) {
        _tareas.value = _tareas.value + tarea
    }

    fun obtenerTareasPorFecha(fecha: Long): List<Tarea> {
        return _tareas.value.filter { it.fecha == fecha }
    }

    fun obtenerTareaPorId(id: String): Tarea? {
        return _tareas.value.find { it.id == id }
    }

    fun marcarTareaComoCompletada(tareaId: String) {
        _tareas.value = _tareas.value.map { tarea ->
            if (tarea.id == tareaId) {
                tarea.copy(completada = true)
            } else {
                tarea
            }
        }
    }

    fun eliminarTarea(tareaId: String) {
        _tareas.value = _tareas.value.filter { it.id != tareaId }
    }

    fun limpiarTareas() {
        _tareas.value = emptyList()
    }
}