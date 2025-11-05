package com.example.tomatetutiempo.presentation.creartarea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.model.Curso
import com.example.tomatetutiempo.data.model.Tarea
import com.example.tomatetutiempo.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class CreateTaskUiState(
    val cursos: List<Curso> = emptyList(),
    val cursoSeleccionado: Curso? = null,
    val nombreTarea: String = "",
    val horasNecesarias: String = "",
    val descripcion: String = "",
    val fechaSeleccionada: Long? = null,
    val mostrarDialogNuevoCurso: Boolean = false,
    val nombreNuevoCurso: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class CreateTaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState.asStateFlow()

    init {
        cargarCursos()
    }

    private fun cargarCursos() {
        viewModelScope.launch {
            // Por ahora, cursos de ejemplo
            // Más adelante esto se conectará con Firebase/Repository
            val cursosEjemplo = listOf(
                Curso(id = "1", nombre = "Cálculo 1", color = "#4CAF50"),
                Curso(id = "2", nombre = "Física 1", color = "#2196F3"),
                Curso(id = "3", nombre = "POO", color = "#FF9800"),
                Curso(id = "4", nombre = "Estadística", color = "#9C27B0")
            )
            _uiState.value = _uiState.value.copy(cursos = cursosEjemplo)
        }
    }

    fun onCursoSeleccionado(curso: Curso) {
        _uiState.value = _uiState.value.copy(cursoSeleccionado = curso)
    }

    fun onNombreTareaChanged(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreTarea = nombre)
    }

    fun onHorasChanged(horas: String) {
        // Solo permitir números
        if (horas.isEmpty() || horas.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(horasNecesarias = horas)
        }
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.value = _uiState.value.copy(descripcion = descripcion)
    }

    fun onFechaSeleccionada(fecha: Long) {
        _uiState.value = _uiState.value.copy(fechaSeleccionada = fecha)
    }

    fun mostrarDialogNuevoCurso() {
        _uiState.value = _uiState.value.copy(mostrarDialogNuevoCurso = true)
    }

    fun ocultarDialogNuevoCurso() {
        _uiState.value = _uiState.value.copy(
            mostrarDialogNuevoCurso = false,
            nombreNuevoCurso = ""
        )
    }

    fun onNombreNuevoCursoChanged(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreNuevoCurso = nombre)
    }

    fun crearNuevoCurso() {
        viewModelScope.launch {
            val nombreCurso = _uiState.value.nombreNuevoCurso.trim()
            if (nombreCurso.isNotEmpty()) {
                val nuevoCurso = Curso(
                    id = UUID.randomUUID().toString(),
                    nombre = nombreCurso,
                    color = generarColorAleatorio()
                )
                val cursosActualizados = _uiState.value.cursos + nuevoCurso
                _uiState.value = _uiState.value.copy(
                    cursos = cursosActualizados,
                    cursoSeleccionado = nuevoCurso,
                    mostrarDialogNuevoCurso = false,
                    nombreNuevoCurso = ""
                )
            }
        }
    }

    fun guardarTarea(): Boolean {
        val state = _uiState.value

        // Validaciones
        if (state.cursoSeleccionado == null) {
            _uiState.value = _uiState.value.copy(error = "Selecciona un curso")
            return false
        }
        if (state.nombreTarea.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Ingresa el nombre de la tarea")
            return false
        }
        if (state.fechaSeleccionada == null) {
            _uiState.value = _uiState.value.copy(error = "Selecciona una fecha")
            return false
        }
        if (state.horasNecesarias.isBlank() || state.horasNecesarias.toIntOrNull() == null) {
            _uiState.value = _uiState.value.copy(error = "Ingresa las horas necesarias")
            return false
        }

        // Crear tarea
        val nuevaTarea = Tarea(
            id = UUID.randomUUID().toString(),
            nombre = state.nombreTarea,
            cursoId = state.cursoSeleccionado.id,
            cursoNombre = state.cursoSeleccionado.nombre,
            fecha = state.fechaSeleccionada,
            horasNecesarias = state.horasNecesarias.toInt(),
            descripcion = state.descripcion
        )

        // Guardar en el repositorio
        TaskRepository.agregarTarea(nuevaTarea)
        println("Tarea guardada: $nuevaTarea")

        limpiarFormulario()
        return true
    }

    fun limpiarFormulario() {
        _uiState.value = CreateTaskUiState(cursos = _uiState.value.cursos)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun generarColorAleatorio(): String {
        val colores = listOf(
            "#4CAF50", "#2196F3", "#FF9800", "#9C27B0",
            "#E91E63", "#00BCD4", "#FFEB3B", "#795548"
        )
        return colores.random()
    }
}