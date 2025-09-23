package com.example.tomatetutiempo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores personalizados
private val Green = Color(0xFF2E7D32)
private val GreenLight = Color(0xFFE8F5E9)
private val TextGray = Color(0xFF4A4A4A)
private val MutedGray = Color(0xFF777777)

// Datos de ejemplo
data class DayChip(val day: String, val dow: String, val marks: String = "")

private val sampleDays = listOf(
    DayChip("03", "Wed", "*"),
    DayChip("04", "Thu", "*"),
    DayChip("05", "Fri", "*"),
    DayChip("06", "Sat", "*"),
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
                        text = "Calendario",
                        color = Green,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
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
                            .border(1.dp, Green, RoundedCornerShape(8.dp))
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
                            text = d.dow,
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
                    Text("Cálculo 1", color = Green, fontWeight = FontWeight.Bold)
                    Text("Hoja de trabajo 2 - 25 Ejercicios", color = TextGray)
                    Text("2:30 horas", color = MutedGray, fontSize = 12.sp)
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
