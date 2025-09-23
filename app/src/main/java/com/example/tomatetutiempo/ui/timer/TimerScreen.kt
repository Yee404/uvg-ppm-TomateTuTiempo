package com.example.tomatetutiempo.ui.timer

import android.util.Log // Importación para Logcat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause // Ícono de Pausa
import androidx.compose.material.icons.filled.PlayArrow // Ícono de Play
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // Para el efecto del temporizador
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf // Para isTimerRunning
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tomatetutiempo.R // Importación para la clase R
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme
import com.example.tomatetutiempo.ui.theme.LightGreenBackground
import com.example.tomatetutiempo.ui.theme.DarkGreenText
import com.example.tomatetutiempo.ui.theme.IconColor
import kotlinx.coroutines.delay // Para la función delay
import java.util.Locale

/**
 * Formatea un número total de segundos a una cadena de texto en formato HH:MM:SS o MM:SS.
 * Si el tiempo es menor a una hora, se omite la sección de horas (MM:SS).
 *
 * @param totalSeconds El número total de segundos a formatear.
 * @return Una cadena de texto representando el tiempo formateado (e.g., "01:23:45" o "23:45").
 */
fun formatTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}

/**
 * Composable que muestra el tiempo del temporizador de forma destacada.
 * Utiliza la función [formatTime] para dar formato al tiempo.
 *
 * @param timeInSeconds El tiempo actual en segundos para mostrar.
 */
@Composable
fun TimerDisplay(timeInSeconds: Int) {
    Text(
        text = formatTime(timeInSeconds),
        fontSize = 90.sp,
        fontWeight = FontWeight.Bold,
        color = DarkGreenText,
        textAlign = TextAlign.Center
    )
}

/**
 * Composable que muestra los controles del temporizador: disminuir tiempo, play/pausa y aumentar tiempo.
 * Los botones de aumentar y disminuir tiempo se deshabilitan cuando el temporizador está en curso.
 *
 * @param isTimerRunning Indica si el temporizador está actualmente en funcionamiento.
 * @param onDecrease Lambda que se invoca cuando se presiona el botón de disminuir tiempo.
 * @param onIncrease Lambda que se invoca cuando se presiona el botón de aumentar tiempo.
 * @param onPlayPause Lambda que se invoca cuando se presiona el botón de play/pausa, alternando el estado del temporizador.
 */
@Composable
fun TimerControls(
    isTimerRunning: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onPlayPause: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onDecrease, enabled = !isTimerRunning) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = stringResource(R.string.timer_decrease_time_description),
                tint = if (!isTimerRunning) IconColor else Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        IconButton(onClick = onPlayPause, modifier = Modifier.size(80.dp)) {
            Icon(
                imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isTimerRunning) stringResource(R.string.timer_pause_timer_description) else stringResource(R.string.timer_start_timer_description),
                tint = DarkGreenText,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        IconButton(onClick = onIncrease, enabled = !isTimerRunning) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.timer_increase_time_description),
                tint = if (!isTimerRunning) IconColor else Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/**
 * Pantalla principal del temporizador Pomodoro.
 * Permite al usuario iniciar, pausar y ajustar un temporizador.
 * Muestra información de una tarea de ejemplo y un botón para finalizar la sesión del temporizador.
 *
 * Esta pantalla utiliza un [Scaffold] para la estructura básica, con una [TopAppBar]
 * para la navegación hacia atrás y un cuerpo de pantalla que contiene el [TimerDisplay],
 * [TimerControls], información de la tarea y un botón de "Finalizado".
 *
 * El estado del temporizador ([timerValueInSeconds] y [isTimerRunning]) se gestiona con [remember] y [mutableStateOf].
 * Un [LaunchedEffect] se encarga de la lógica de decremento del tiempo cuando el temporizador está activo.
 *
 * @param navController El controlador de navegación de Jetpack Compose, utilizado para la acción de "volver atrás".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navController: NavController) {
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.timer_back_button_description),
                            tint = DarkGreenText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Fondo transparente para la TopAppBar.
                    navigationIconContentColor = DarkGreenText // Color del ícono de navegación.
                )
            )
        },
        containerColor = LightGreenBackground // Color de fondo para la pantalla.
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // Aplica el padding proporcionado por Scaffold.
                .fillMaxSize()
                .padding(16.dp), // Padding general para el contenido de la columna.
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Distribuye el espacio entre los elementos.
        ) {
            // Sección superior: Información de la tarea y visualización del temporizador.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f) // Ocupa el espacio disponible, empujando el botón Finalizado hacia abajo.
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Textos de ejemplo para la tarea.
                Text(stringResource(R.string.timer_task_name_example), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = DarkGreenText)
                Text(stringResource(R.string.timer_task_time_example), fontSize = 18.sp, color = DarkGreenText)
                Text(stringResource(R.string.timer_task_detail_example), fontSize = 20.sp, color = DarkGreenText, modifier = Modifier.padding(bottom = 70.dp))

                TimerDisplay(timeInSeconds = timerValueInSeconds)

                Spacer(modifier = Modifier.height(20.dp))

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
                    Log.d("TimerScreen", "Botón FINALIZADO presionado. Tiempo: ${formatTime(timerValueInSeconds)}")
                    isTimerRunning = false // Detiene el temporizador al finalizar.
                    // TODO: Implementar lógica real para finalizar la sesión (e.g., navegar, guardar datos).
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkGreenText
                ),
                border = BorderStroke(1.dp, DarkGreenText),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(44.dp)
            ) {
                Text(stringResource(R.string.timer_finish_button_text), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Composable de previsualización para la [TimerScreen].
 * Muestra cómo se verá la pantalla del temporizador en el entorno de diseño de Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TomateTuTiempoTheme {
        TimerScreen(rememberNavController())
    }
}
