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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = { Text("Calendario", color = VerdePrincipal, fontWeight = FontWeight.SemiBold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = VerdePrincipal)
                    }
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

            if (uiState.tareasDelDia.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay tareas para este día", color = TextoGrisClaro, fontSize = 16.sp)
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

    if (uiState.mostrarDialogEditar && uiState.tareaSeleccionada != null) {
        DialogEditarTarea(
            tarea = uiState.tareaSeleccionada!!,
            onDismiss = { viewModel.cerrarDialogEditar() },
            onSave = { nombre, horas, descripcion ->
                viewModel.actualizarTarea(nombre, horas, descripcion)
            },
            onDelete = {
                viewModel.eliminarTareaSeleccionada()
            }
        )
    }
}

@Composable
fun DayChip(dia: DiaCalendario, isSelected: Boolean, onClick: () -> Unit) {
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
        Text(dia.dia, color = if (isSelected) Color.White else TextoGris, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(dia.diaSemana.take(3), color = if (isSelected) Color.White.copy(alpha = 0.9f) else TextoGrisClaro, fontSize = 12.sp)
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
fun TarjetaTarea(tarea: Tarea, onClick: () -> Unit, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (tarea.completada) VerdeClaro else Color.White),
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
                    Text(tarea.cursoNombre, color = VerdePrincipal, fontWeight = FontWeight.Bold, fontSize = 18.sp, textDecoration = if (tarea.completada) TextDecoration.LineThrough else null)
                    if (tarea.completada) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = "Completada", tint = VerdePrincipal, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(tarea.nombre, color = TextoGris, fontSize = 16.sp, textDecoration = if (tarea.completada) TextDecoration.LineThrough else null)
                Spacer(modifier = Modifier.height(8.dp))
                Text("${tarea.horasNecesarias} ${if (tarea.horasNecesarias == 1) "hora" else "horas"}", color = TextoGrisClaro, fontSize = 14.sp)
            }
            if (!tarea.completada) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = VerdePrincipal)
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
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(tarea.cursoNombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VerdePrincipal)
                Spacer(modifier = Modifier.height(8.dp))
                Text(tarea.nombre, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = TextoGris)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tiempo estimado: ", fontSize = 14.sp, color = TextoGrisClaro)
                    Text("${tarea.horasNecesarias} ${if (tarea.horasNecesarias == 1) "hora" else "horas"}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = VerdePrincipal)
                }
                if (tarea.descripcion.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Descripción:", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextoGris)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(tarea.descripcion, fontSize = 14.sp, color = TextoGrisClaro, lineHeight = 20.sp)
                }
                if (tarea.completada) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(VerdeClaro, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, "Completada", tint = VerdePrincipal, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tarea completada", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = VerdePrincipal)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                        Text("Cerrar")
                    }

                    if (!tarea.completada) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onIniciarTarea, colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal), shape = RoundedCornerShape(8.dp)) {
                            Text("Iniciar")
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
    onSave: (String, Int, String) -> Unit,
    onDelete: () -> Unit
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
                Text("Editar Tarea", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VerdePrincipal)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePrincipal, focusedLabelColor = VerdePrincipal)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = horas,
                    onValueChange = { if (it.all { char -> char.isDigit() }) horas = it },
                    label = { Text("Horas necesarias") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePrincipal, focusedLabelColor = VerdePrincipal)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VerdePrincipal, focusedLabelColor = VerdePrincipal)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text("Eliminar")
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                val horasInt = horas.toIntOrNull() ?: 1
                                onSave(nombre, horasInt, descripcion)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                            enabled = nombre.isNotBlank() && horas.isNotBlank(),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}