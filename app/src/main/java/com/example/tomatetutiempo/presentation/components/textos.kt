package com.example.tomatetutiempo.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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
            .padding( 16.dp)

    )
}
