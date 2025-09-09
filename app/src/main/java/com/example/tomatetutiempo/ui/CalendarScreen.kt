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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* Colores */
private val Green = Color(0xFF2E7D32)
private val GreenLight = Color(0xFFEAF5E6)
private val TextGray = Color(0xFF4A4A4A)
private val MutedGray = Color(0xFF777777)

/* Datos demo */
data class DayChip(val day: String, val dow: String, val marks: String = "")
private val sampleDays = listOf(
    DayChip("03", "Wed", "*"),
    DayChip("04", "Thu", "**"),
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
                    Text("Calendario", color = Green, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "menu", tint = Green)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "profile", tint = Green)
                    }
                }
            )
        },
        containerColor = GreenLight
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text("Septiembre", color = Green, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                items(sampleDays) { d ->
                    DayChipItem(d, selected == d.day) { selected = d.day }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimelineBar(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 8.dp)
                        .height(160.dp)
                        .width(24.dp)
                )
                TaskCard(
                    title = "Cálculo 1",
                    subtitle = "Hoja de trabajo 2 - 25 Ejercicios",
                    duration = "2:30 horas"
                )
            }
        }
    }
}

/* Componentes */
@Composable
private fun DayChipItem(data: DayChip, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Green else Color.White
    val fg = if (selected) Color.White else Green
    Card(
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 4.dp else 2.dp),
        modifier = Modifier
            .size(width = 72.dp, height = 88.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(data.day, color = fg, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(data.dow, color = fg, fontSize = 14.sp)
            if (data.marks.isNotEmpty()) {
                Text(data.marks, color = fg, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun TaskCard(title: String, subtitle: String, duration: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    color = Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .border(width = 2.dp, color = Green, shape = RoundedCornerShape(3.dp))
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = TextGray, fontSize = 14.sp)
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(duration, color = MutedGray, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Icon(Icons.Default.PlayArrow, contentDescription = "play", tint = Green)
            }
        }
    }
}

@Composable
private fun TimelineBar(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(16.dp).clip(CircleShape).background(Green))
        Spacer(Modifier.width(4.dp).weight(1f).background(Green.copy(alpha = 0.7f)))
        Box(Modifier.size(20.dp).clip(CircleShape).background(Color(0xFF9BE24D)))
    }
}

/* Preview */
@Preview(showBackground = true, backgroundColor = 0xFFEAF5E6)
@Composable
private fun PreviewCalendarScreen() { CalendarScreen() }
