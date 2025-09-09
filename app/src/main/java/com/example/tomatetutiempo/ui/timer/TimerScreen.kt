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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme
import com.example.tomatetutiempo.ui.theme.LightGreenBackground
import com.example.tomatetutiempo.ui.theme.DarkGreenText
import com.example.tomatetutiempo.ui.theme.IconColor
import kotlinx.coroutines.delay // Para la función delay
import java.util.Locale

/**
 * Formatea un número total de segundos a una cadena de texto en formato HH:MM:SS o MM:SS.
 *
 * @param totalSeconds El número total de segundos a formatear.
 * @return Una cadena de texto representando el tiempo formateado.
 */
private fun formatTime(totalSeconds: Int): String {
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
 * Composable que muestra el tiempo del temporizador formateado.
 *
 * @param timeInSeconds El tiempo actual en segundos para mostrar.
 */
@Composable
private fun TimerDisplay(timeInSeconds: Int) {
    Text(
        text = formatTime(timeInSeconds),
        fontSize = 90.sp,
        fontWeight = FontWeight.Bold,
        color = DarkGreenText,
        textAlign = TextAlign.Center
    )
}

/**
 * Composable que muestra los controles del temporizador.
 *
 * @param isTimerRunning Indica si el temporizador está actualmente en funcionamiento.
 * @param onDecrease Lambda que se invoca cuando se presiona el botón de disminuir tiempo.
 * @param onIncrease Lambda que se invoca cuando se presiona el botón de aumentar tiempo.
 * @param onPlayPause Lambda que se invoca cuando se presiona el botón de play/pausa.
 */
@Composable
private fun TimerControls(
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
        /**
         * IconButton para disminuir el tiempo del temporizador.
         * Al presionarlo, invoca la lambda [onDecrease].
         */
        IconButton(onClick = onDecrease, enabled = !isTimerRunning) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Disminuir tiempo", // TODO: Usar stringResource
                tint = if (!isTimerRunning) IconColor else Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        /**
         * IconButton para iniciar o pausar el temporizador.
         * Al presionarlo, invoca la lambda [onPlayPause].
         * El ícono cambia entre Play y Pause según el estado [isTimerRunning].
         */
        IconButton(onClick = onPlayPause, modifier = Modifier.size(80.dp)) {
            Icon(
                imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isTimerRunning) "Pausar temporizador" else "Iniciar temporizador", // TODO: Usar stringResource
                tint = DarkGreenText,
                modifier = Modifier.size(60.dp) // Tamaño del ícono Play/Pause
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        /**
         * IconButton para aumentar el tiempo del temporizador.
         * Al presionarlo, invoca la lambda [onIncrease].
         */
        IconButton(onClick = onIncrease, enabled = !isTimerRunning) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Aumentar tiempo", // TODO: Usar stringResource
                tint = if (!isTimerRunning) IconColor else Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/**
 * Pantalla principal del temporizador. Muestra información de la tarea, el temporizador,
 * controles para ajustar el tiempo y un botón para finalizar la sesión.
 *
 * @param navController El controlador de navegación para manejar acciones como volver atrás.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navController: NavController) {
    var timerValueInSeconds by remember { mutableIntStateOf(4736) } // 1h 18m 56s
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = timerValueInSeconds, key2 = isTimerRunning) {
        if (isTimerRunning && timerValueInSeconds > 0) {
            delay(1000L) // Espera 1 segundo
            timerValueInSeconds--
        } else if (timerValueInSeconds == 0 && isTimerRunning) {
            isTimerRunning = false // Detiene el temporizador si llega a 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title needed */ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás", // TODO: Usar stringResource
                            tint = DarkGreenText,
                            modifier = Modifier.size(20.dp) // Tamaño del ícono de volver ajustado
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = DarkGreenText
                )
            )
        },
        containerColor = LightGreenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text("Cálculo I", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = DarkGreenText)
                Text("(2:00pm - 4:00pm)", fontSize = 18.sp, color = DarkGreenText)
                Text("Hoja de Trabajo 2", fontSize = 20.sp, color = DarkGreenText, modifier = Modifier.padding(bottom = 70.dp))

                TimerDisplay(timeInSeconds = timerValueInSeconds)

                Spacer(modifier = Modifier.height(20.dp))

                TimerControls(
                    isTimerRunning = isTimerRunning,
                    onDecrease = {
                        if (!isTimerRunning && timerValueInSeconds >= 60) {
                            timerValueInSeconds -= 60
                        } else if (!isTimerRunning) {
                            timerValueInSeconds = 0
                        }
                    },
                    onIncrease = {
                        if (!isTimerRunning) {
                            timerValueInSeconds += 60
                        }
                    },
                    onPlayPause = {
                        isTimerRunning = !isTimerRunning
                    }
                )
            }

            /**
             * Botón para finalizar la sesión del temporizador.
             * Estilo minimalista y moderno usando OutlinedButton.
             */
            OutlinedButton(
                onClick = {
                    Log.d("TimerScreen", "Botón FINALIZADO presionado. Tiempo: ${formatTime(timerValueInSeconds)}")
                    isTimerRunning = false // Detener el temporizador al finalizar
                    // TODO: Lógica real para finalizar (navegar, guardar, etc.)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkGreenText // Color del texto y el borde cuando no está presionado
                ),
                border = BorderStroke(1.dp, DarkGreenText), // Grosor y color del borde
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp) // Padding vertical reducido
                    .height(44.dp) // Altura reducida
            ) {
                Text("FINALIZADO", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) // Ajuste de fuente
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TomateTuTiempoTheme {
        TimerScreen(rememberNavController())
    }
}
