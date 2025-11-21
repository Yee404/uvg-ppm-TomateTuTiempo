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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tomatetutiempo.presentation.theme.WelcomeColors
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = viewModel(),
    onAddTaskClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
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

                if (uiState.isLoading) {
                } else {
                    ProfileHeader(
                        userName = uiState.user?.name ?: "Usuario",
                        profileImageRes = android.R.drawable.ic_menu_camera
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Sección de botones del menú
                MenuSection(
                    onAddTaskClick = onAddTaskClick,
                    onCalendarClick = onCalendarClick,
                    onStoreClick = onStoreClick,
                    onProfileClick = onProfileClick
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
