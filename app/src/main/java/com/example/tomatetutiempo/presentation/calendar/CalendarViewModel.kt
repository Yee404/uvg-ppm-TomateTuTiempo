package com.example.tomatetutiempo.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.model.Tarea
import com.example.tomatetutiempo.data.repository.TaskRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class DiaCalendario(
    val fecha: Long,
    val dia: String,
    val diaSemana: String,
    val tieneTareas: Boolean = false
)

data class CalendarUiState(
    val dias: List<DiaCalendario> = emptyList(),
    val fechaSeleccionada: Long = 0L,
    val tareasDelDia: List<Tarea> = emptyList(),
    val tareaSeleccionada: Tarea? = null,
    val mostrarDialogDetalle: Boolean = false,
    val mostrarDialogEditar: Boolean = false
)

class CalendarViewModel(
    private val taskRepository: TaskRepository = TaskRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        cargarDiasDelMes()
        observarTareas()
    }

    private fun observarTareas() {
        viewModelScope.launch {
            taskRepository.tareas.collect { todasLasTareas ->
                val diasActualizados = _uiState.value.dias.map { dia ->
                    dia.copy(tieneTareas = todasLasTareas.any { tarea ->
                        isSameDay(tarea.fecha, dia.fecha)
                    })
                }

                val tareasDelDia = todasLasTareas.filter { tarea ->
                    isSameDay(tarea.fecha, _uiState.value.fechaSeleccionada)
                }

                _uiState.value = _uiState.value.copy(
                    dias = diasActualizados,
                    tareasDelDia = tareasDelDia
                )
            }
        }
    }

    private fun isSameDay(timestamp: Timestamp?, fechaLong: Long): Boolean {
        if (timestamp == null) return false
        val cal1 = Calendar.getInstance().apply { time = timestamp.toDate() }
        val cal2 = Calendar.getInstance().apply { timeInMillis = fechaLong }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun cargarDiasDelMes() {
        val dias = mutableListOf<DiaCalendario>()
        val sdfDia = SimpleDateFormat("dd", Locale.getDefault())
        val sdfDiaSemana = SimpleDateFormat("EEEE", Locale("es", "ES"))

        for (i in 0..6) {
            val fecha = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, i)
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val tempCal = Calendar.getInstance().apply { timeInMillis = fecha }
            dias.add(
                DiaCalendario(
                    fecha = fecha,
                    dia = sdfDia.format(tempCal.time),
                    diaSemana = sdfDiaSemana.format(tempCal.time).replaceFirstChar { it.uppercase() },
                    tieneTareas = false
                )
            )
        }

        val fechaHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        _uiState.value = _uiState.value.copy(
            dias = dias,
            fechaSeleccionada = fechaHoy
        )
    }

    fun seleccionarDia(fecha: Long) {
        val todasLasTareas = taskRepository.tareas.value
        val tareasDelDia = todasLasTareas.filter { tarea ->
            isSameDay(tarea.fecha, fecha)
        }
        _uiState.value = _uiState.value.copy(
            fechaSeleccionada = fecha,
            tareasDelDia = tareasDelDia
        )
    }

    fun marcarTareaComoCompletada(tareaId: String, completada: Boolean) {
        viewModelScope.launch {
            taskRepository.marcarTareaComoCompletada(tareaId, completada)
        }
    }

    fun eliminarTarea(tareaId: String) {
        viewModelScope.launch {
            taskRepository.eliminarTarea(tareaId)
        }
    }

    fun actualizarTarea(nombre: String, horas: Int, descripcion: String) {
        viewModelScope.launch {
            _uiState.value.tareaSeleccionada?.let { tarea ->
                taskRepository.actualizarTarea(
                    tareaId = tarea.id,
                    nombre = nombre,
                    horasNecesarias = horas,
                    descripcion = descripcion
                )
            }
        }
    }

    fun mostrarDetalleTarea(tarea: Tarea) {
        _uiState.value = _uiState.value.copy(tareaSeleccionada = tarea, mostrarDialogDetalle = true)
    }

    fun cerrarDialogDetalle() {
        _uiState.value = _uiState.value.copy(tareaSeleccionada = null, mostrarDialogDetalle = false)
    }

    fun mostrarDialogEditarTarea(tarea: Tarea) {
        _uiState.value = _uiState.value.copy(tareaSeleccionada = tarea, mostrarDialogEditar = true)
    }

    fun cerrarDialogEditar() {
        _uiState.value = _uiState.value.copy(tareaSeleccionada = null, mostrarDialogEditar = false)
    }
}