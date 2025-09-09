package com.example.tomatetutiempo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tomatetutiempo.ui.CalendarScreen
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TomateTuTiempoTheme {
                CalendarScreen()   // 👈 MOSTRAR tu pantalla, no Greeting()
            }
        }
    }
}
