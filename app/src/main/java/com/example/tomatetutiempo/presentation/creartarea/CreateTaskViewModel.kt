package com.example.tomatetutiempo.presentation.creartarea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.model.Curso
import com.example.tomatetutiempo.data.model.Tarea
import com.example.tomatetutiempo.data.repository.CourseRepository
import com.example.tomatetutiempo.data.repository.TaskRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
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

class CreateTaskViewModel(
    private val courseRepository: CourseRepository = CourseRepository(),
    private val taskRepository: TaskRepository = TaskRepository()

) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState.asStateFlow()

    init {
        cargarCursos()
    }

    private fun cargarCursos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val cursosDesdeFirebase = courseRepository.obtenerCursos()
            _uiState.value = _uiState.value.copy(cursos = cursosDesdeFirebase, isLoading = false)
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
            val cursoYaExiste = _uiState.value.cursos.any { it.nombre.equals(nombreCurso, ignoreCase = true) }

            if (nombreCurso.isNotEmpty() && !cursoYaExiste) {
                val nuevoCurso = Curso(
                    nombre = nombreCurso,
                    color = generarColorAleatorio()
                )

                courseRepository.agregarCurso(nuevoCurso)
                cargarCursos()

                _uiState.value = _uiState.value.copy(
                    mostrarDialogNuevoCurso = false,
                    nombreNuevoCurso = ""
                )
            } else if (cursoYaExiste) {
                _uiState.value = _uiState.value.copy(error = "Ese curso ya existe")
            }
        }
    }

    fun guardarTarea(): Boolean {
        val state = _uiState.value

        val curso = state.cursoSeleccionado
        val fecha = state.fechaSeleccionada
        val horas = state.horasNecesarias.toIntOrNull()

        if (curso == null || state.nombreTarea.isBlank() || fecha == null || horas == null) {
            _uiState.value = _uiState.value.copy(error = "Completa todos los campos")
            return false
        }

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
            nombre = state.nombreTarea,
            cursoId = curso.id,
            cursoNombre = curso.nombre,
            fecha = Timestamp(Date(fecha)),
            horasNecesarias = horas,
            descripcion = state.descripcion,
            completada = false
        )

        // Guardar en el repositorio
        viewModelScope.launch {
            taskRepository.agregarTarea(nuevaTarea)
        }

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