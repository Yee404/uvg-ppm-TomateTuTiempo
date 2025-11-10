package com.example.tomatetutiempo.presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tomatetutiempo.presentation.components.ProfileImage
import com.example.tomatetutiempo.presentation.theme.WelcomeColors
import com.example.tomatetutiempo.R

@Composable
fun ProfileHeader(
    userName: String,
    profileImageRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(
            imageRes = profileImageRes,
            size = 80.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.welcome_greeting),
            color = WelcomeColors.TextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = userName,
            color = WelcomeColors.TextColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}