package com.example.tomatetutiempo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.* //SocialButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier //SocialButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SocialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    container: Color,
    content: Color,
    border: BorderStroke? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = container, contentColor = content),
        border = border
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}
