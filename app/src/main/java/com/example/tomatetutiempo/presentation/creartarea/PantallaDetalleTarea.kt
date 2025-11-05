package com.example.tomatetutiempo.presentation.creartarea

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleTarea(
    viewModel: CreateTaskViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onTareaGuardada: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Formatear fecha seleccionada
    val fechaTexto = remember(uiState.fechaSeleccionada) {
        uiState.fechaSeleccionada?.let {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            sdf.format(Date(it))
        } ?: ""
    }

    // Obtener fecha actual en milisegundos (inicio del día)
    val fechaActual = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdePrincipal
                ),
                title = {
                    Column {
                        Text(
                            text = "Nombre",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = uiState.cursoSeleccionado?.nombre ?: "",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdePrincipal)
                .padding(padding)
        ) {
            // Contenido desplazable en card blanca
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Fecha
                        Column {
                            Text(
                                text = "Fecha",
                                fontSize = 14.sp,
                                color = TextoGrisClaro,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = {
                                    val calendar = Calendar.getInstance()
                                    uiState.fechaSeleccionada?.let {
                                        calendar.timeInMillis = it
                                    }

                                    val datePickerDialog = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            val selectedCalendar = Calendar.getInstance().apply {
                                                set(year, month, dayOfMonth, 0, 0, 0)
                                                set(Calendar.MILLISECOND, 0)
                                            }
                                            viewModel.onFechaSeleccionada(selectedCalendar.timeInMillis)
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    )

                                    // No permitir fechas anteriores a hoy
                                    datePickerDialog.datePicker.minDate = fechaActual
                                    datePickerDialog.show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = VerdeClaro,
                                    contentColor = VerdePrincipal
                                )
                            ) {
                                Text(
                                    text = if (fechaTexto.isNotEmpty()) fechaTexto else "Seleccionar fecha",
                                    fontSize = 16.sp,
                                    fontWeight = if (fechaTexto.isNotEmpty()) FontWeight.Medium else FontWeight.Normal
                                )
                            }
                        }

                        // Tiempo necesario
                        Column {
                            Text(
                                text = "Tiempo necesario",
                                fontSize = 14.sp,
                                color = TextoGrisClaro,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = uiState.horasNecesarias,
                                onValueChange = viewModel::onHorasChanged,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text("2 horas", color = TextoGrisClaro)
                                },
                                suffix = {
                                    Text("horas", color = TextoGris)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VerdePrincipal,
                                    unfocusedBorderColor = Color.LightGray,
                                    cursorColor = VerdePrincipal,
                                    focusedContainerColor = VerdeClaro.copy(alpha = 0.3f),
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        // Nombre de la tarea
                        Column {
                            Text(
                                text = "Nombre de la tarea",
                                fontSize = 14.sp,
                                color = TextoGrisClaro,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = uiState.nombreTarea,
                                onValueChange = viewModel::onNombreTareaChanged,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text("Nombre de la tarea...", color = TextoGrisClaro)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VerdePrincipal,
                                    unfocusedBorderColor = Color.LightGray,
                                    cursorColor = VerdePrincipal,
                                    focusedContainerColor = VerdeClaro.copy(alpha = 0.3f),
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        // Descripción
                        Column {
                            Text(
                                text = "Descripción",
                                fontSize = 14.sp,
                                color = TextoGrisClaro,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = uiState.descripcion,
                                onValueChange = viewModel::onDescripcionChanged,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                placeholder = {
                                    Text("Descripción de la tarea...", color = TextoGrisClaro)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = VerdePrincipal,
                                    unfocusedBorderColor = Color.LightGray,
                                    cursorColor = VerdePrincipal,
                                    focusedContainerColor = VerdeClaro.copy(alpha = 0.3f),
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp),
                                maxLines = 5
                            )
                        }
                    }

                    // Botón crear tarea
                    Button(
                        onClick = {
                            if (viewModel.guardarTarea()) {
                                onTareaGuardada()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdePrincipal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Create Task",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // Mostrar error si existe
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Aquí podrías mostrar un Snackbar
            println("Error: $error")
            viewModel.clearError()
        }
    }
}