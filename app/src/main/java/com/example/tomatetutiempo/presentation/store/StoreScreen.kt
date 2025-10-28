package com.example.tomatetutiempo.presentation.store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart

// 👇 esto es para el Preview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StoreScreen(
    viewModel: StoreViewModel = viewModel()
) {
    // agarramos el estado actual del ViewModel
    val state by viewModel.state

    // Surface para manejar el fondo
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF2DB84C) // verde de fondo (puedes cambiarlo por tu verde del tema)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (state.cargando) {
                // circulito de "loading"
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Título "Tienda"
                    Text(
                        text = "Tienda",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Lista de productos
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.productos) { producto ->
                            ProductoItem(producto = producto)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // aquí después pueden poner navegar a detalles, comprar, etc.
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9) // verde muy clarito tipo botón
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cuadrito blanco con icono a la izquierda, estilo tus botones
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFF2DB84C),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF1B1B1B)
                )
                Text(
                    text = producto.descripcion,
                    fontSize = 13.sp,
                    color = Color(0xFF4A4A4A)
                )
            }

            Text(
                text = producto.precio,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2DB84C),
                fontSize = 14.sp
            )
        }
    }
}

// ======================
// PREVIEW PARA DISEÑO 👇
// ======================

@Preview(showBackground = true)
@Composable
fun PreviewStoreScreen() {
    // Para el preview, no usamos el ViewModel real (porque viewModel() da problemas en preview).
    // Creamos un estado falso aquí mismo:

    val fakeState = StoreState(
        productos = listOf(
            Producto(
                id = 1,
                nombre = "Pomodoro Pro",
                descripcion = "Temporizador premium para estudio.",
                precio = "Q29.99"
            ),
            Producto(
                id = 2,
                nombre = "Paquete de stickers",
                descripcion = "Stickers motivacionales digitales.",
                precio = "Q9.99"
            )
        ),
        cargando = false
    )

    // Vamos a reutilizar la misma UI de arriba pero sin ViewModel:
    PreviewStoreScreenContent(fakeState)
}

@Composable
private fun PreviewStoreScreenContent(state: StoreState) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF2DB84C)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Tienda",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.productos) { producto ->
                        ProductoItem(producto = producto)
                    }
                }
            }
        }
    }
}
