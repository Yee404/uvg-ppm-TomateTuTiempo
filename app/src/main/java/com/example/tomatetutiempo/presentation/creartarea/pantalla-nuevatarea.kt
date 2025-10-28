package com.example.tomatetutiempo.presentation.creartarea

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.presentation.components.CampoDeTexto
import com.example.tomatetutiempo.presentation.components.SelectorFecha
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke


// ==== Colores consistentes con el resto de la app ====
val Green = Color(0xFF2E7D32)
val GreenLight = Color(0xFFE8F5E9)
val DarkerGreen = Color(0xFF1B5E20)
val TextGray = Color(0xFF4A4A4A)
val Orange = Color(0xFFD56B41)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNuevaTarea() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenLight),
                title = {
                    Text(
                        text = stringResource(R.string.titulo), // “Nueva tarea”
                        color = Green,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* abrir menú lateral si se desea */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu_icon_desc),
                            tint = Green
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GreenLight)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Contenido desplazable
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.crea_una_nueva_tarea),
                    color = TextGray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                // Campos de texto
                CampoDeTexto(label = stringResource(R.string.ingr_nombre_de_la_tarea))
                CampoDeTexto(label = stringResource(R.string.ingre_tema_de_tarea))
                CampoDeTexto(label = stringResource(R.string.ing_descripcion_de_tarea))

                // Selector de fecha adaptado
                SelectorFecha()
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja los botones hacia el final

            // === Botón Guardar tarea ===
            OutlinedButton(
                onClick = {
                    // TODO: Lógica para guardar la tarea
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Green,
                    containerColor = Green.copy(alpha = 0.3f)
                ),
                border = BorderStroke(2.dp, Green),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Guardar tarea",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // === Botón Borrar tarea ===
            OutlinedButton(
                onClick = {
                    // TODO: Lógica para borrar la tarea
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Orange,
                    containerColor = Orange.copy(alpha = 0.2f)
                ),
                border = BorderStroke(2.dp, Orange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Borrar tarea",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaNuevaTarea() {
    PantallaNuevaTarea()
}
