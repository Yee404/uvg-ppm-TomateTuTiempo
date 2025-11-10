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
import androidx.compose.ui.res.stringResource
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.Naranja

@Composable
fun SelectorFecha(
    onFechaSeleccionada: ((Long) -> Unit)? = null
) {
    val contexto = LocalContext.current
    var fechaSeleccionada by remember { mutableStateOf("") }

    val calendario = Calendar.getInstance()
    val annio = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)

    // Obtener fecha actual en milisegundos (inicio del día)
    val fechaActual = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val datePickerDialog = DatePickerDialog(
        contexto,
        { _, year, month, dayOfMonth ->
            fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"

            // Callback con timestamp en milisegundos
            onFechaSeleccionada?.let {
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                it(selectedCalendar.timeInMillis)
            }
        },
        annio, mes, dia
    )

    // No permitir seleccionar fechas anteriores a hoy
    datePickerDialog.datePicker.minDate = fechaActual

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