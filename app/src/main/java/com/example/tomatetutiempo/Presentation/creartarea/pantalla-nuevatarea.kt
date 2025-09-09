package com.example.tomatetutiempo.Presentation.creartarea

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.Presentation.Componentes.MenuButton
import com.example.tomatetutiempo.Presentation.Componentes.PerfilButton
import com.example.tomatetutiempo.Presentation.Componentes.FAB

@Composable
fun PantallaNuevaTarea() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF5E6))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            MenuButton()
            Text(text = "Crea Una Tarea",
                color = Color(0xFF2E7C32),
                fontSize = 18.sp)
            PerfilButton()
        }

        FAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(vertical = 32.dp)

        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaNuevaTarea() {
    PantallaNuevaTarea()
}
