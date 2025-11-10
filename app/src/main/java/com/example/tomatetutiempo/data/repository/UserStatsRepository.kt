package com.example.tomatetutiempo.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

data class UserStats(
    val gems: Int = 0,
    val completedTasks: Int = 0,
    val streak: Int = 0,
    val lastCompletionDate: Long = 0L, // timestamp del último día que completó una tarea
    val name: String = "Sara Rodriguez",
    val email: String = ""
)

/**
 * Repositorio Singleton para manejar las estadísticas del usuario
 */
object UserStatsRepository {

    private const val POINTS_PER_TASK = 300

    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    /**
     * Se llama cuando el usuario completa una tarea
     */
    fun onTaskCompleted() {
        val currentStats = _userStats.value
        val today = getTodayTimestamp()

        // Calcular nueva racha
        val newStreak = calculateNewStreak(currentStats.lastCompletionDate, today, currentStats.streak)

        // Actualizar estadísticas
        _userStats.value = currentStats.copy(
            gems = currentStats.gems + POINTS_PER_TASK,
            completedTasks = currentStats.completedTasks + 1,
            streak = newStreak,
            lastCompletionDate = today
        )
    }

    /**
     * Calcula la nueva racha basándose en la fecha de la última tarea completada
     */
    private fun calculateNewStreak(lastDate: Long, todayDate: Long, currentStreak: Int): Int {
        if (lastDate == 0L) {
            // Primera tarea completada
            return 1
        }

        val daysDifference = getDaysDifference(lastDate, todayDate)

        return when {
            daysDifference == 0L -> currentStreak // Misma fecha, mantener racha
            daysDifference == 1L -> currentStreak + 1 // Día consecutivo, incrementar
            else -> 1 // Se rompió la racha, reiniciar a 1
        }
    }

    /**
     * Obtiene el timestamp del inicio del día actual
     */
    private fun getTodayTimestamp(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    /**
     * Calcula la diferencia en días entre dos timestamps
     */
    private fun getDaysDifference(date1: Long, date2: Long): Long {
        val millisecondsPerDay = 24 * 60 * 60 * 1000L
        return (date2 - date1) / millisecondsPerDay
    }

    /**
     * Para resetear las estadísticas (útil para testing)
     */
    fun resetStats() {
        _userStats.value = UserStats()
    }

    /**
     * Para cargar estadísticas guardadas (cuando implementes Firebase)
     */
    fun loadStats(stats: UserStats) {
        _userStats.value = stats
    }
}