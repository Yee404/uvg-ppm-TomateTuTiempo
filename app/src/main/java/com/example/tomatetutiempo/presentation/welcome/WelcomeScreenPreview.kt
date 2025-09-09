package com.example.tomatetutiempo.presentation.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme

// @Preview(showBackground = true)
// @Composable
// fun WelcomeScreenPreview() {
//    TomateTuTiempoTheme {
//        WelcomeScreen(
//           userName = "Sara Rodriguez",
//            profileImageRes = android.R.drawable.ic_menu_camera,
//           onAddTaskClick = { /* Preview - no action */ },
//           onCalendarClick = { /* Preview - no action */ },
//           onStoreClick = { /* Preview - no action */ },
//           onSettingsClick = { /* Preview - no action */ },
//           onProfileClick = { /* Preview - no action */ }
//        )
//    }
//}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenFullPreview() {
    TomateTuTiempoTheme {
        WelcomeScreen(
            userName = "Sara Rodriguez",
            profileImageRes = android.R.drawable.ic_menu_camera
        )
    }
}