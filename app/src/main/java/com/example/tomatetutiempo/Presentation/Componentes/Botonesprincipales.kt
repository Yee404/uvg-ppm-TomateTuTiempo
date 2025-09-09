package com.example.tomatetutiempo.Presentation.Componentes

import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tomatetutiempo.R
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MenuButton() {
    Button(
        onClick = { /* ir a el menu */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Fondo blanco
    ) {
        Icon(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "MENU",
            modifier = Modifier
                .size(45.dp),
            tint = Color.Unspecified,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PerfilButton() {
    Button(
        onClick = { /* ir a el perfil */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Fondo blanco
    ) {
        Icon(
            painter = painterResource(id = R.drawable.usuario),
            contentDescription = "user",
            modifier = Modifier
                .size(45.dp),
            tint = Color.Unspecified,
        )
    }
}

@Composable
fun FAB(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            Toast.makeText(context, "Se Elimino la Tarea", Toast.LENGTH_SHORT).show()
        },
        modifier = modifier.width(120.dp),
        containerColor = Color(0xFFE65F3A),
        contentColor = Color.Black
    ) {
        Text("Eliminar Tarea")
    }
}