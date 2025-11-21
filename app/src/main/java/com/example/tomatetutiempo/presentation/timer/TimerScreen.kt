package com.example.tomatetutiempo.presentation.timer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomatetutiempo.ui.theme.DarkGreenText
import com.example.tomatetutiempo.ui.theme.IconColor
import com.example.tomatetutiempo.ui.theme.LightGreenBackground
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    taskId: String,
    onNavigateBack: () -> Unit,
    viewModel: TimerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = taskId) {
        viewModel.loadTask(taskId)
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
                            tint = DarkGreenText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = LightGreenBackground
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkGreenText)
            }
        } else if (uiState.tarea == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: Tarea no encontrada")
            }
        } else {
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

                    Text(uiState.tarea!!.cursoNombre, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = DarkGreenText)
                    Text(uiState.tarea!!.nombre, fontSize = 20.sp, color = DarkGreenText)
                    if (uiState.tarea!!.descripcion.isNotBlank()) {
                        Text(uiState.tarea!!.descripcion, fontSize = 16.sp, color = DarkGreenText.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 70.dp))
                    }

                    TimerDisplay(timeInSeconds = uiState.tiempoRestanteSegundos)

                    Spacer(modifier = Modifier.height(20.dp))

                    TimerControls(
                        isTimerRunning = uiState.isTimerRunning,
                        onPlayPause = { viewModel.onPlayPause() },
                        onDecrease = { viewModel.onDecreaseTime() },
                        onIncrease = { viewModel.onIncreaseTime() }
                    )
                }

                OutlinedButton(
                    onClick = {
                        viewModel.onFinish()
                        onNavigateBack()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreenText),
                    border = BorderStroke(1.dp, DarkGreenText),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(44.dp)
                ) {
                    Text("FINALIZADO", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        if (uiState.mostrarDialogoFinal) {
            AlertDialog(
                onDismissRequest = { viewModel.onDismissDialog() },
                title = { Text("¿Finalizaste la tarea?") },
                text = { Text("Marca la tarea como completada o añade más tiempo.") },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onConfirmFinish() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreenText),
                        ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onAddMoreTime() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreenText),
                        ) {
                        Text("+10 min")
                    }
                }
            )
        }
    }
}

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

@Composable
fun TimerControls(
    isTimerRunning: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onPlayPause: () -> Unit
) {
    val buttonColor = if (!isTimerRunning) IconColor else Color.Gray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onDecrease, enabled = !isTimerRunning) {
            Icon(Icons.Default.Remove, "Disminuir", tint = buttonColor, modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.width(30.dp))

        IconButton(onClick = onPlayPause, modifier = Modifier.size(80.dp)) {
            Icon(
                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isTimerRunning) "Pausar" else "Iniciar",
                tint = DarkGreenText,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        IconButton(onClick = onIncrease, enabled = !isTimerRunning) {
            Icon(Icons.Default.Add, "Aumentar", tint = buttonColor, modifier = Modifier.size(48.dp))
        }
    }
}

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