package com.example.tomatetutiempo.presentation.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.data.repository.TaskRepository
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// Colores
private val VerdeFondo = Color(0xFFDEEFE0)
private val VerdePrincipal = Color(0xFF5FA777)
private val VerdeOscuro = Color(0xFF4A8B5C)
private val TextoGris = Color(0xFF4A4A4A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    taskName: String = "",
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Obtener la tarea del repositorio
    val tarea = remember(taskName) {
        TaskRepository.obtenerTareaPorId(taskName)
    }

    // Calcular tiempo inicial en segundos desde las horas de la tarea
    val tiempoInicialSegundos = (tarea?.horasNecesarias ?: 1) * 3600

    var timerValueInSeconds by remember { mutableIntStateOf(tiempoInicialSegundos) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Efecto para el temporizador
    LaunchedEffect(key1 = timerValueInSeconds, key2 = isTimerRunning) {
        if (isTimerRunning && timerValueInSeconds > 0) {
            delay(1000L)
            timerValueInSeconds--
        } else if (timerValueInSeconds == 0 && isTimerRunning) {
            isTimerRunning = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = VerdeOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = VerdeFondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Información de la tarea
            Text(
                text = tarea?.cursoNombre ?: "Tarea",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Horario de la tarea
            val sdfHora = SimpleDateFormat("h:mm a", Locale.getDefault())
            val horaInicio = Calendar.getInstance().time
            val horaFin = Calendar.getInstance().apply {
                add(Calendar.HOUR_OF_DAY, tarea?.horasNecesarias ?: 1)
            }.time

            Text(
                text = "(${sdfHora.format(horaInicio)} - ${sdfHora.format(horaFin)})",
                fontSize = 16.sp,
                color = VerdeOscuro.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = tarea?.nombre ?: "Sin nombre",
                fontSize = 18.sp,
                color = VerdeOscuro.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Display del temporizador
            Text(
                text = formatTime(timerValueInSeconds),
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Controles del temporizador
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón disminuir
                IconButton(
                    onClick = {
                        if (!isTimerRunning && timerValueInSeconds >= 60) {
                            timerValueInSeconds -= 60
                        }
                    },
                    enabled = !isTimerRunning,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Disminuir tiempo",
                        tint = if (!isTimerRunning) VerdeOscuro else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(48.dp))

                // Botón Play/Pause (circular con borde)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(3.dp, VerdeOscuro, CircleShape)
                        .background(Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { isTimerRunning = !isTimerRunning },
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isTimerRunning) "Pausar" else "Iniciar",
                            tint = VerdeOscuro,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(48.dp))

                // Botón aumentar
                IconButton(
                    onClick = {
                        if (!isTimerRunning) {
                            timerValueInSeconds += 60
                        }
                    },
                    enabled = !isTimerRunning,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Aumentar tiempo",
                        tint = if (!isTimerRunning) VerdeOscuro else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Finalizado
            OutlinedButton(
                onClick = {
                    isTimerRunning = false
                    tarea?.let {
                        TaskRepository.marcarTareaComoCompletada(it.id)
                    }
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = VerdeOscuro,
                    containerColor = Color.Transparent
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(VerdeOscuro)
                )
            ) {
                Text(
                    text = "FINALIZADO",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

private fun formatTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%01d:%02d:%02d", hours, minutes, seconds)
}