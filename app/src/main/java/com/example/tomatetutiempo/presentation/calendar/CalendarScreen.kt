package com.example.tomatetutiempo.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.R

// Colores personalizados
private val Green = Color(0xFF2E7D32)
private val GreenLight = Color(0xFFE8F5E9)
private val TextGray = Color(0xFF4A4A4A)
private val MutedGray = Color(0xFF777777)

// Datos de ejemplo
data class DayChip(val day: String, val dowStringRes: Int, val marks: String = "")

private val sampleDays = listOf(
    DayChip("03", R.string.dia_miercoles, "*"),
    DayChip("04", R.string.dia_jueves, "*"),
    DayChip("05", R.string.dia_viernes, "*"),
    DayChip("06", R.string.dia_sabado, "*"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf("03") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenLight),
                title = {
                    Text(
                        text = stringResource(R.string.calendario_titulo),
                        color = Green,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.menu_icon_desc))
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GreenLight)
                .padding(padding)
        ) {
            // Fila de días
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleDays) { d ->
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (d.day == selected) Green else Color.White)
                            .border(
                                1.dp,
                                Green,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selected = d.day }
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = d.day,
                            color = if (d.day == selected) Color.White else TextGray,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(d.dowStringRes),
                            color = if (d.day == selected) Color.White else MutedGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Tarjeta de evento
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.evento_calculo1), color = Green, fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.evento_hoja_trabajo), color = TextGray)
                    Text(stringResource(R.string.evento_duracion), color = MutedGray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    CalendarScreen(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendar() {
    CalendarScreen()
}