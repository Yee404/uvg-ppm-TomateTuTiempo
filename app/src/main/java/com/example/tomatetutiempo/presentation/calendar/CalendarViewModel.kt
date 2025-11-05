package com.example.tomatetutiempo.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.model.Tarea
import com.example.tomatetutiempo.data.repository.TaskRepository
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
    val mostrarDialogDetalle: Boolean = false
)

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        cargarDiasDelMes()
        observarTareas()
    }

    private fun observarTareas() {
        viewModelScope.launch {
            TaskRepository.tareas.collect { tareas ->
                // Actualizar días con tareas
                val diasActualizados = _uiState.value.dias.map { dia ->
                    dia.copy(tieneTareas = tareas.any { it.fecha == dia.fecha })
                }
                _uiState.value = _uiState.value.copy(
                    dias = diasActualizados,
                    tareasDelDia = tareas.filter { it.fecha == _uiState.value.fechaSeleccionada }
                )
            }
        }
    }

    private fun cargarDiasDelMes() {
        val calendar = Calendar.getInstance()
        val hoy = calendar.timeInMillis

        // Generar 7 días a partir de hoy
        val dias = mutableListOf<DiaCalendario>()
        val sdfDia = SimpleDateFormat("dd", Locale.getDefault())
        val sdfDiaSemana = SimpleDateFormat("EEEE", Locale("es", "ES"))

        for (i in 0..6) {
            val fecha = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, i)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val tempCal = Calendar.getInstance().apply { timeInMillis = fecha }
            val dia = sdfDia.format(tempCal.time)
            val diaSemana = sdfDiaSemana.format(tempCal.time).capitalize(Locale.getDefault())

            dias.add(
                DiaCalendario(
                    fecha = fecha,
                    dia = dia,
                    diaSemana = diaSemana,
                    tieneTareas = false
                )
            )
        }

        // Seleccionar hoy por defecto
        val fechaHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        _uiState.value = _uiState.value.copy(
            dias = dias,
            fechaSeleccionada = fechaHoy,
            tareasDelDia = TaskRepository.obtenerTareasPorFecha(fechaHoy)
        )
    }

    fun seleccionarDia(fecha: Long) {
        val tareasDelDia = TaskRepository.obtenerTareasPorFecha(fecha)
        _uiState.value = _uiState.value.copy(
            fechaSeleccionada = fecha,
            tareasDelDia = tareasDelDia
        )
    }

    fun mostrarDetalleTarea(tarea: Tarea) {
        _uiState.value = _uiState.value.copy(
            tareaSeleccionada = tarea,
            mostrarDialogDetalle = true
        )
    }

    fun cerrarDialogDetalle() {
        _uiState.value = _uiState.value.copy(
            tareaSeleccionada = null,
            mostrarDialogDetalle = false
        )
    }
}