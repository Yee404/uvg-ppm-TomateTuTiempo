package com.example.tomatetutiempo.presentation.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomatetutiempo.data.model.Tarea
import com.example.tomatetutiempo.data.repository.TaskRepository
import com.example.tomatetutiempo.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TimerUiState(
    val tarea: Tarea? = null,
    val tiempoRestanteSegundos: Int = 0,
    val isTimerRunning: Boolean = false,
    val isLoading: Boolean = true,
    val mostrarDialogoFinal: Boolean = false
)

class TimerViewModel(
    private val taskRepository: TaskRepository = TaskRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.tareas.first { it.isNotEmpty() }

            val tarea = taskRepository.tareas.value.find { it.id == taskId }

            if (tarea != null) {
                val tiempoInicial = tarea.horasNecesarias * 3600
                _uiState.value = TimerUiState(
                    tarea = tarea,
                    tiempoRestanteSegundos = tiempoInicial,
                    isLoading = false
                )
            } else {
                _uiState.value = TimerUiState(isLoading = false)
            }
        }
    }

    fun onPlayPause() {
        val isRunning = _uiState.value.isTimerRunning
        if (isRunning) {
            timerJob?.cancel()
        } else {
            startTimer()
        }
        _uiState.value = _uiState.value.copy(isTimerRunning = !isRunning)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_uiState.value.tiempoRestanteSegundos > 0) {
                delay(1000L)
                _uiState.value = _uiState.value.copy(
                    tiempoRestanteSegundos = _uiState.value.tiempoRestanteSegundos - 1
                )
            }
            _uiState.value = _uiState.value.copy(
                isTimerRunning = false,
                mostrarDialogoFinal = true
            )
        }
    }

    fun onFinish() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isTimerRunning = false)

        viewModelScope.launch {
            val tareaId = _uiState.value.tarea?.id
            if (tareaId != null) {
                taskRepository.marcarTareaComoCompletada(tareaId, true)
                userRepository.onTaskCompleted()
            }
        }
    }

    fun onIncreaseTime() {
        if (!_uiState.value.isTimerRunning) {
            _uiState.value = _uiState.value.copy(
                tiempoRestanteSegundos = _uiState.value.tiempoRestanteSegundos + 600
            )
        }
    }

    fun onDecreaseTime() {
        if (!_uiState.value.isTimerRunning) {
            val nuevoTiempo = (_uiState.value.tiempoRestanteSegundos - 600).coerceAtLeast(0)
            _uiState.value = _uiState.value.copy(
                tiempoRestanteSegundos = nuevoTiempo
            )
        }
    }

    fun onConfirmFinish() {
        _uiState.value = _uiState.value.copy(mostrarDialogoFinal = false)
        onFinish()
    }

    fun onAddMoreTime() {
        _uiState.value = _uiState.value.copy(
            tiempoRestanteSegundos = _uiState.value.tiempoRestanteSegundos + 600, // 10 minutos
            mostrarDialogoFinal = false
        )
        onPlayPause()
    }

    fun onDismissDialog() {
        _uiState.value = _uiState.value.copy(mostrarDialogoFinal = false)
    }
}