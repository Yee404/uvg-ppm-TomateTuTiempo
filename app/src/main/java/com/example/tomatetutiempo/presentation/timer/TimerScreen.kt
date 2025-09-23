package com.example.tomatetutiempo.presentation.timer

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.DarkGreenText
import com.example.tomatetutiempo.ui.theme.IconColor
import com.example.tomatetutiempo.ui.theme.LightGreenBackground
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme
import kotlinx.coroutines.delay
import java.util.Locale


fun formatTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.Companion.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.Companion.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}

@Composable
fun TimerDisplay(timeInSeconds: Int) {
    Text(
        text = formatTime(timeInSeconds),
        fontSize = 90.sp,
        fontWeight = FontWeight.Companion.Bold,
        color = DarkGreenText,
        textAlign = TextAlign.Companion.Center
    )
}

@Composable
fun TimerControls(
    isTimerRunning: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onPlayPause: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        IconButton(onClick = onDecrease, enabled = !isTimerRunning) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = stringResource(R.string.timer_decrease_time_description),
                tint = if (!isTimerRunning) IconColor else Color.Companion.Gray,
                modifier = Modifier.Companion.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.Companion.width(30.dp))

        IconButton(onClick = onPlayPause, modifier = Modifier.Companion.size(80.dp)) {
            Icon(
                imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isTimerRunning) stringResource(R.string.timer_pause_timer_description) else stringResource(
                    R.string.timer_start_timer_description
                ),
                tint = DarkGreenText,
                modifier = Modifier.Companion.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.Companion.width(30.dp))

        IconButton(onClick = onIncrease, enabled = !isTimerRunning) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.timer_increase_time_description),
                tint = if (!isTimerRunning) IconColor else Color.Companion.Gray,
                modifier = Modifier.Companion.size(48.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    taskName: String = "Tarea",
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var timerValueInSeconds by remember { mutableIntStateOf(4736) } // Valor inicial de ejemplo: 1h 18m 56s
    var isTimerRunning by remember { mutableStateOf(false) }

    // Efecto para manejar la lógica del temporizador cuando está activo.
    LaunchedEffect(key1 = timerValueInSeconds, key2 = isTimerRunning) {
        if (isTimerRunning && timerValueInSeconds > 0) {
            delay(1000L) // Espera 1 segundo.
            timerValueInSeconds-- // Decrementa el tiempo.
        } else if (timerValueInSeconds == 0 && isTimerRunning) {
            isTimerRunning = false // Detiene el temporizador si llega a 0.
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No se necesita título para esta pantalla. */ },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.timer_back_button_description),
                            tint = DarkGreenText,
                            modifier = Modifier.Companion.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Companion.Transparent, // Fondo transparente para la TopAppBar.
                    navigationIconContentColor = DarkGreenText // Color del ícono de navegación.
                )
            )
        },
        containerColor = LightGreenBackground // Color de fondo para la pantalla.
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .padding(innerPadding) // Aplica el padding proporcionado por Scaffold.
                .fillMaxSize()
                .padding(16.dp), // Padding general para el contenido de la columna.
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Distribuye el espacio entre los elementos.
        ) {
            // Sección superior: Información de la tarea y visualización del temporizador.
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion.weight(1f) // Ocupa el espacio disponible, empujando el botón Finalizado hacia abajo.
            ) {
                Spacer(modifier = Modifier.Companion.height(16.dp))

                // Textos de ejemplo para la tarea.
                Text(
                    taskName, // USAR EL PARÁMETRO taskName EN LUGAR DEL STRING FIJO
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = DarkGreenText
                )
                Text(
                    stringResource(R.string.timer_task_time_example),
                    fontSize = 18.sp,
                    color = DarkGreenText
                )
                Text(
                    stringResource(R.string.timer_task_detail_example),
                    fontSize = 20.sp,
                    color = DarkGreenText,
                    modifier = Modifier.Companion.padding(bottom = 70.dp)
                )

                TimerDisplay(timeInSeconds = timerValueInSeconds)

                Spacer(modifier = Modifier.Companion.height(20.dp))

                TimerControls(
                    isTimerRunning = isTimerRunning,
                    onDecrease = {
                        if (!isTimerRunning && timerValueInSeconds >= 60) { // Solo disminuye si no está corriendo y hay tiempo.
                            timerValueInSeconds -= 60
                        } else if (!isTimerRunning) { // Si es menor a 60, va a 0.
                            timerValueInSeconds = 0
                        }
                    },
                    onIncrease = {
                        if (!isTimerRunning) { // Solo aumenta si no está corriendo.
                            timerValueInSeconds += 60
                        }
                    },
                    onPlayPause = {
                        isTimerRunning = !isTimerRunning // Alterna el estado del temporizador.
                    }
                )
            }

            // Botón para finalizar la sesión del temporizador.
            OutlinedButton(
                onClick = {
                    Log.d(
                        "TimerScreen",
                        "Botón FINALIZADO presionado. Tiempo: ${formatTime(timerValueInSeconds)}"
                    )
                    isTimerRunning = false // Detiene el temporizador al finalizar.
                    // TODO: Implementar lógica real para finalizar la sesión (e.g., navegar, guardar datos).
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkGreenText
                ),
                border = BorderStroke(1.dp, DarkGreenText),
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(44.dp)
            ) {
                Text(
                    stringResource(R.string.timer_finish_button_text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Companion.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TomateTuTiempoTheme {
        TimerScreen(
            taskName = "Calculo 1",
            onNavigateBack = {}
        )
    }
}