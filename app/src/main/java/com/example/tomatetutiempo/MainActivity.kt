package com.example.tomatetutiempo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tomatetutiempo.presentation.login.Login
import com.example.tomatetutiempo.presentation.welcome.WelcomeScreen
import com.example.tomatetutiempo.presentation.creartarea.PantallaNuevaTarea
import com.example.tomatetutiempo.presentation.calendar.CalendarScreen
import com.example.tomatetutiempo.presentation.timer.TimerScreen
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomateTuTiempoTheme {
                NavigationApp()
            }
        }
    }
}

@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Login(
                onLoginSuccess = { navController.navigate("welcome") }
            )
        }

        composable("welcome") {
            WelcomeScreen(
                onAddTaskClick = { navController.navigate("nuevaTarea") },
                onCalendarClick = { navController.navigate("calendar") },
                onStoreClick = { /* Puedes agregar después */ },
                onSettingsClick = { /* Puedes agregar después */ },
                onProfileClick = { /* Puedes agregar después */ }
            )
        }

        composable("nuevaTarea") {
            PantallaNuevaTarea()
        }

        composable("calendar") {
            CalendarScreen(
                onTaskSelected = { taskName ->
                    navController.navigate("timer/$taskName")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "timer/{taskName}",
            arguments = listOf(navArgument("taskName") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskName = backStackEntry.arguments?.getString("taskName") ?: ""
            TimerScreen(
                taskName = taskName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}