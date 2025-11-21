package com.example.tomatetutiempo.presentation.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomatetutiempo.ui.theme.VerdePrincipal
import com.example.tomatetutiempo.ui.theme.VerdeFondo
import com.example.tomatetutiempo.ui.theme.TextoOscuro
import com.example.tomatetutiempo.ui.theme.TextoClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    viewModel: StoreViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tienda", color = VerdePrincipal, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = VerdePrincipal)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFondo)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tus gemas", fontSize = 14.sp, color = TextoClaro)
                        Text("${state.gemasUsuario} 💎", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = VerdePrincipal)
                    }

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Artículos disponibles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextoOscuro)

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.items) { item ->
                    StoreItemCard(
                        item = item,
                        tieneGemasSuficientes = state.gemasUsuario >= item.costoGemas,
                        onComprarClick = { viewModel.comprar(item.id) }
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.descripcion, fontSize = 13.sp, color = TextoClaro)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onComprarClick,
                enabled = !item.comprado && tieneGemasSuficientes,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = when {
                        item.comprado -> "Adquirido"
                        else -> "${item.costoGemas}"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}