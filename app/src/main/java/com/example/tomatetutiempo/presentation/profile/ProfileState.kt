package com.example.tomatetutiempo.ui.presentation.profile

data class ProfileState(
    val isLoading: Boolean = true,
    val name: String = "",
    val email: String = "",
    val gems: Int = 0,
    val completedTasks: Int = 0,
    val streak: Int = 0
)