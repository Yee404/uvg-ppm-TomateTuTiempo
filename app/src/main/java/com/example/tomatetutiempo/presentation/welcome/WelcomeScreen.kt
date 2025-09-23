package com.example.tomatetutiempo.presentation.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tomatetutiempo.presentation.theme.WelcomeColors
import com.example.tomatetutiempo.R

@Composable
fun WelcomeScreen(
    userName: String = stringResource(R.string.preview_user_name),
    profileImageRes: Int = android.R.drawable.ic_menu_camera, // Placeholder
    onAddTaskClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WelcomeColors.BackgroundGradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Header con foto de perfil y saludo
                ProfileHeader(
                    userName = userName,
                    profileImageRes = profileImageRes
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Sección de botones del menú
                MenuSection(
                    onAddTaskClick = onAddTaskClick,
                    onCalendarClick = onCalendarClick,
                    onStoreClick = onStoreClick,
                    onSettingsClick = onSettingsClick,
                    onProfileClick = onProfileClick
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}