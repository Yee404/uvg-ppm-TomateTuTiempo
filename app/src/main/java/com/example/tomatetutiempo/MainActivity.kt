package com.example.tomatetutiempo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tomatetutiempo.presentation.login.Login
import com.example.tomatetutiempo.presentation.register.RegisterScreen
import com.example.tomatetutiempo.presentation.welcome.WelcomeScreen
import com.example.tomatetutiempo.presentation.creartarea.PantallaCrearTarea
import com.example.tomatetutiempo.presentation.creartarea.PantallaDetalleTarea
import com.example.tomatetutiempo.presentation.creartarea.CreateTaskViewModel
import com.example.tomatetutiempo.presentation.calendar.CalendarScreen
import com.example.tomatetutiempo.presentation.calendar.CalendarViewModel
import com.example.tomatetutiempo.presentation.timer.TimerScreen
import com.example.tomatetutiempo.presentation.store.StoreScreen
import com.example.tomatetutiempo.ui.presentation.profile.PerfilScreen
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

    // ViewModel compartido para las pantallas de crear tarea
    val createTaskViewModel: CreateTaskViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Login(
                onLoginSuccess = { navController.navigate("welcome") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Después del registro exitoso, ir a welcome
                    navController.navigate("welcome") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("welcome") {
            WelcomeScreen(
                onAddTaskClick = {
                    createTaskViewModel.limpiarFormulario()
                    navController.navigate("crearTarea")
                },
                onCalendarClick = { navController.navigate("calendar") },
                onStoreClick = { navController.navigate("store") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

        composable("crearTarea") {
            PantallaCrearTarea(
                viewModel = createTaskViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCursoSeleccionado = {
                    navController.navigate("detalleTarea")
                }
            )
        }

        composable("detalleTarea") {
            PantallaDetalleTarea(
                viewModel = createTaskViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTareaGuardada = {
                    navController.navigate("welcome") {
                        popUpTo("crearTarea") { inclusive = true }
                    }
                }
            )
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

        composable("store") {
            StoreScreen()
        }

        composable("profile") {
            PerfilScreen(
                onEditProfileClick = {
                    // TODO: Navegar a editar perfil cuando esté listo
                },
                onSettingsClick = {
                    // TODO: Navegar a ajustes cuando esté listo
                },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
    }
}