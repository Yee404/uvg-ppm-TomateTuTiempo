package com.example.tomatetutiempo.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val gems: Int = 0,
    val completedTasks: Int = 0,
    val streak: Int = 0,
    val lastCompletionDate: Timestamp? = null
)