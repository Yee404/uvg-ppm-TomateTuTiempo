package com.example.tomatetutiempo.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object WelcomeColors {
    val GradientStart = Color(0xFF388E3C)
    val GradientEnd = Color(0xFF66BB6A)
    val IconColor = Color(0xFF81C784)
    val TextColor = Color.White
    val ButtonBackground = Color.White.copy(alpha = 0.2f)

    val BackgroundGradient = Brush.verticalGradient(
        colors = listOf(GradientStart, GradientEnd)
    )
}

