package com.example.tomatetutiempo.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun CampoDeTexto(
    label: String,
    valorInicial: String = ""
) {
    var texto by remember { mutableStateOf(valorInicial) }

    OutlinedTextField(
        value = texto,
        onValueChange = { texto = it },
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2E7D32),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFF2E7D32),
            unfocusedLabelColor = Color.DarkGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
