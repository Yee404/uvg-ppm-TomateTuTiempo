package com.example.tomatetutiempo.presentation.creartarea

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomatetutiempo.data.model.Curso

// Colores
val VerdePrincipal = Color(0xFF5FA777)
val VerdeClaro = Color(0xFFE8F5E9)
val VerdeFondo = Color(0xFFF1F8F4)
val TextoGris = Color(0xFF4A4A4A)
val TextoGrisClaro = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearTarea(
    viewModel: CreateTaskViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onCursoSeleccionado: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarDialogNuevoCurso by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido principal
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    title = {
                        Text(
                            text = "Crear tarea",
                            color = VerdePrincipal,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = VerdePrincipal
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VerdeFondo)
                    .padding(padding)
                    .blur(if (mostrarDialogNuevoCurso) 4.dp else 0.dp)
            ) {
                // Título
                Text(
                    text = "Selecciona el curso",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextoGris,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )

                // Lista de cursos
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.cursos) { curso ->
                        TarjetaCurso(
                            curso = curso,
                            onClick = {
                                viewModel.onCursoSeleccionado(curso)
                                onCursoSeleccionado()
                            }
                        )
                    }
                }

                // Botón crear nuevo curso
                Button(
                    onClick = { mostrarDialogNuevoCurso = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear nuevo curso",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Dialog para crear nuevo curso (overlay)
        AnimatedVisibility(
            visible = mostrarDialogNuevoCurso,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) { }
            ) {
                DialogNuevoCurso(
                    nombreCurso = uiState.nombreNuevoCurso,
                    onNombreChanged = viewModel::onNombreNuevoCursoChanged,
                    onConfirmar = {
                        viewModel.crearNuevoCurso()
                        mostrarDialogNuevoCurso = false
                    },
                    onCancelar = {
                        viewModel.ocultarDialogNuevoCurso()
                        mostrarDialogNuevoCurso = false
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaCurso(
    curso: Curso,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de color del curso
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(android.graphics.Color.parseColor(curso.color)),
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = curso.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextoGris
            )
        }
    }
}

@Composable
fun BoxScope.DialogNuevoCurso(
    nombreCurso: String,
    onNombreChanged: (String) -> Unit,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    Card(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.85f)
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
            Text(
                text = "Inserta el nombre de la clase",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoGris
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = nombreCurso,
                onValueChange = onNombreChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Escribe el nombre...",
                        color = TextoGrisClaro
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdePrincipal,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = VerdePrincipal
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onCancelar,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = TextoGris
                    )
                ) {
                    Text("Cancelar", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onConfirmar,
                    enabled = nombreCurso.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdePrincipal
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Aceptar", fontSize = 16.sp)
                }
            }
        }
    }
}