package com.example.tomatetutiempo.presentation.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    viewModel: StoreViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.limpiarMensaje()
        }
    }

    StoreScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onComprarClick = { id -> viewModel.comprar(id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoreScreenContent(
    state: StoreState,
    snackbarHostState: SnackbarHostState,
    onComprarClick: (String) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F2FF)) // fondo lila suave
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Título
            Text(
                text = "Tienda",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1C)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tarjeta de gemas
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE9F7FF)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Tus gemas",
                            fontSize = 14.sp,
                            color = Color(0xFF4A4A4A)
                        )
                        Text(
                            text = "${state.gemasUsuario}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0077C2)
                        )
                    }
                    Text(
                        text = "Completa tareas para ganar más",
                        fontSize = 12.sp,
                        color = Color(0xFF6B6B6B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Artículos disponibles",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3C3C3C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.items) { item ->
                    StoreItemCard(
                        item = item,
                        tieneGemasSuficientes = state.gemasUsuario >= item.costoGemas,
                        onComprarClick = { onComprarClick(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreItemCard(
    item: StoreItem,
    tieneGemasSuficientes: Boolean,
    onComprarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAFBE7)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F3B2F)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.descripcion,
                    fontSize = 12.sp,
                    color = Color(0xFF4F5F53)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${item.costoGemas} 💎",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2A7E3B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                val estaComprado = item.comprado
                val habilitado = !estaComprado && tieneGemasSuficientes

                Button(
                    onClick = onComprarClick,
                    enabled = habilitado,
                    shape = RoundedCornerShape(999.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when {
                            estaComprado -> "Comprado"
                            !tieneGemasSuficientes -> "Sin gemas"
                            else -> "Comprar"
                        },
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/* ------------ PREVIEW SOLO UI (SIN VIEWMODEL) ------------- */

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Preview Tienda"
)
@Composable
fun PreviewStoreScreen() {
    val fakeState = StoreState(
        gemasUsuario = 80,
        items = listOf(
            StoreItem(
                id = "tema_pastel",
                nombre = "Tema pastel",
                descripcion = "Colores suaves para estudiar más relajado.",
                costoGemas = 30
            ),
            StoreItem(
                id = "tema_oscuro",
                nombre = "Tema oscuro",
                descripcion = "Ideal para estudiar de noche.",
                costoGemas = 40,
                comprado = true
            ),
            StoreItem(
                id = "stickers",
                nombre = "Stickers motivacionales",
                descripcion = "Frases lindas cuando completas tareas.",
                costoGemas = 20
            )
        )
    )

    val snackbarHostState = remember { SnackbarHostState() }

    StoreScreenContent(
        state = fakeState,
        snackbarHostState = snackbarHostState,
        onComprarClick = {}
    )
}
