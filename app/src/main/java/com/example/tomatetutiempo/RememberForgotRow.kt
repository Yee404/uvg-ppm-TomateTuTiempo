package com.example.tomatetutiempo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun RememberForgotRow(
    remember: Boolean,
    onRememberChange: (Boolean) -> Unit,
    onForgot: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = remember, onCheckedChange = onRememberChange)
            Text(text = stringResource(R.string.remember_me), fontSize = 14.sp)
        }
        TextButton(onClick = onForgot) {
            Text(text = stringResource(R.string.forgotpass), fontSize = 14.sp)
        }
    }
}
