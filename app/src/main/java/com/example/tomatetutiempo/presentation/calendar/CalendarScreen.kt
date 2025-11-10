package com.example.tomatetutiempo.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomatetutiempo.data.model.Tarea

// Colores
private val VerdePrincipal = Color(0xFF5FA777)
private val VerdeClaro = Color(0xFFE8F5E9)
private val VerdeFondo = Color(0xFFF1F8F4)
private val TextoGris = Color(0xFF4A4A4A)
private val TextoGrisClaro = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(),
    onTaskSelected: (String) -> Unit = { _ -> },
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Text(
                        text = "Calendario",
                        color = VerdePrincipal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondo)
                .padding(padding)
        ) {
            // Fila de días (horizontal scroll)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.dias) { dia ->
                    DayChip(
                        dia = dia,
                        isSelected = dia.fecha == uiState.fechaSeleccionada,
                        onClick = { viewModel.seleccionarDia(dia.fecha) }
                    )
                }
            }

            // Lista de tareas del día seleccionado
            if (uiState.tareasDelDia.isEmpty()) {
                // Mensaje cuando no hay tareas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay tareas para este día",
                        color = TextoGrisClaro,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.tareasDelDia) { tarea ->
                        TarjetaTarea(
                            tarea = tarea,
                            onClick = { viewModel.mostrarDetalleTarea(tarea) },
                            onEditClick = { viewModel.mostrarDialogEditarTarea(tarea) }
                        )
                    }
                }
            }
        }
    }

    // Dialog de detalle de tarea
    if (uiState.mostrarDialogDetalle && uiState.tareaSeleccionada != null) {
        DialogDetalleTarea(
            tarea = uiState.tareaSeleccionada!!,
            onDismiss = { viewModel.cerrarDialogDetalle() },
            onIniciarTarea = {
                viewModel.cerrarDialogDetalle()
                onTaskSelected(uiState.tareaSeleccionada!!.id)
            }
        )
    }

    // Dialog de editar tarea
    if (uiState.mostrarDialogEditar && uiState.tareaSeleccionada != null) {
        DialogEditarTarea(
            tarea = uiState.tareaSeleccionada!!,
            onDismiss = { viewModel.cerrarDialogEditar() },
            onSave = { nombre, horas, descripcion ->
                viewModel.actualizarTarea(nombre, horas, descripcion)
            }
        )
    }
}

@Composable
fun DayChip(
    dia: DiaCalendario,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) VerdePrincipal else Color.White)
            .border(
                width = if (dia.tieneTareas) 2.dp else 1.dp,
                color = if (isSelected) VerdePrincipal else if (dia.tieneTareas) VerdePrincipal.copy(alpha = 0.5f) else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dia.dia,
            color = if (isSelected) Color.White else TextoGris,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = dia.diaSemana.take(3), // Primeras 3 letras (Lun, Mar, etc)
            color = if (isSelected) Color.White.copy(alpha = 0.9f) else TextoGrisClaro,
            fontSize = 12.sp
        )

        // Indicador de que hay tareas
        if (dia.tieneTareas && !isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(VerdePrincipal, shape = RoundedCornerShape(3.dp))
            )
        }
    }
}

@Composable
fun TarjetaTarea(
    tarea: Tarea,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (tarea.completada) VerdeClaro else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tarea.cursoNombre,
                        color = VerdePrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
                    )
                    if (tarea.completada) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completada",
                            tint = VerdePrincipal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tarea.nombre,
                    color = TextoGris,
                    fontSize = 16.sp,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${tarea.horasNecesarias} ${if (tarea.horasNecesarias == 1) "hora" else "horas"}",
                    color = TextoGrisClaro,
                    fontSize = 14.sp
                )
            }

            // Botón editar (solo si no está completada)
            if (!tarea.completada) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = VerdePrincipal
                    )
                }
            }
        }
    }
}

@Composable
fun DialogDetalleTarea(
    tarea: Tarea,
    onDismiss: () -> Unit,
    onIniciarTarea: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Título del curso
                Text(
                    text = tarea.cursoNombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrincipal
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nombre de la tarea
                Text(
                    text = tarea.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextoGris
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Horas necesarias
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tiempo estimado: ",
                        fontSize = 14.sp,
                        color = TextoGrisClaro
                    )
                    Text(
                        text = "${tarea.horasNecesarias} ${if (tarea.horasNecesarias == 1) "hora" else "horas"}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdePrincipal
                    )
                }

                // Descripción (si existe)
                if (tarea.descripcion.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Descripción:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextoGris
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tarea.descripcion,
                        fontSize = 14.sp,
                        color = TextoGrisClaro,
                        lineHeight = 20.sp
                    )
                }

                // Mostrar si ya está completada
                if (tarea.completada) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(VerdeClaro, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completada",
                            tint = VerdePrincipal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tarea completada",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = VerdePrincipal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón cancelar
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextoGris
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancelar")
                    }

                    // Botón iniciar tarea (solo si no está completada)
                    if (!tarea.completada) {
                        Button(
                            onClick = onIniciarTarea,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdePrincipal
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Iniciar tarea")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogEditarTarea(
    tarea: Tarea,
    onDismiss: () -> Unit,
    onSave: (String, Int, String) -> Unit
) {
    var nombre by remember { mutableStateOf(tarea.nombre) }
    var horas by remember { mutableStateOf(tarea.horasNecesarias.toString()) }
    var descripcion by remember { mutableStateOf(tarea.descripcion) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Editar Tarea",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrincipal
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdePrincipal,
                        focusedLabelColor = VerdePrincipal
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo horas
                OutlinedTextField(
                    value = horas,
                    onValueChange = { if (it.all { char -> char.isDigit() }) horas = it },
                    label = { Text("Horas necesarias") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdePrincipal,
                        focusedLabelColor = VerdePrincipal
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdePrincipal,
                        focusedLabelColor = VerdePrincipal
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val horasInt = horas.toIntOrNull() ?: 1
                            onSave(nombre, horasInt, descripcion)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                        enabled = nombre.isNotBlank() && horas.isNotBlank()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}