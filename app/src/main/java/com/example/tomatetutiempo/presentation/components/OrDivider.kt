package com.example.tomatetutiempo.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrDivider(text: String, color: Color, textColor: Color) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.Companion.weight(1f), color = color)
        Text(
            text = text,
            fontSize = 14.sp,
            color = textColor,
            modifier = Modifier.Companion.padding(horizontal = 8.dp)
        )
        HorizontalDivider(modifier = Modifier.Companion.weight(1f), color = color)
    }
}