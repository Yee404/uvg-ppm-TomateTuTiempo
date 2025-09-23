package com.example.tomatetutiempo.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.ui.res.stringResource
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.Naranja

@Composable
fun SelectorFecha() {
    val contexto = LocalContext.current
    var fechaSeleccionada by remember { mutableStateOf("") }

    val calendario = Calendar.getInstance()
    val annio = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        contexto,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
        },
        annio, mes, dia
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { datePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Naranja,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.seleccionar_fecha))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(stringResource(R.string.fecha_seleccionada, fechaSeleccionada))
    }
}
