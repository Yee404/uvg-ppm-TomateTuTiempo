package com.example.tomatetutiempo.Presentation.Componentes

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tomatetutiempo.R
import androidx.compose.ui.unit.dp



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
