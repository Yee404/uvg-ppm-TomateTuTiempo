package com.example.tomatetutiempo.Presentation.Componentes

import android.widget.Toast
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.Naranja

@Composable
fun FAB(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            Toast.makeText(context,
                context.getString(R.string.msg_tarea_eliminada), Toast.LENGTH_SHORT).show()
        },
        modifier = modifier.width(120.dp),
        containerColor = Naranja,
        contentColor = Color.White
    ) {
        Text(stringResource(R.string.eliminar_tarea))
    }
}