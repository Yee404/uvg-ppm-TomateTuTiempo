package com.example.tomatetutiempo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterFooter(onClick: () -> Unit) {
    val greencolor = Color(0xFF467848)
    val text = buildAnnotatedString {
        append(stringResource(R.string.dont_account))
        withStyle(SpanStyle(color = greencolor, fontWeight = FontWeight.Bold)) {
            append(stringResource(R.string.register))
        }
    }
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center,
        fontSize = 14.sp
    )
}
