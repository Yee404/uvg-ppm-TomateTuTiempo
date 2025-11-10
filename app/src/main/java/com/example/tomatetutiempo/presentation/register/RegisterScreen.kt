package com.example.tomatetutiempo.presentation.register

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.presentation.components.*
import com.example.tomatetutiempo.presentation.theme.LoginColors
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uiState) {
        if (uiState.registerSuccess) {
            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
        }
        uiState.error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    RegisterContent(
        uiState = uiState,
        name = name,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        onNameChange = { name = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegisterClick = {
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isPasswordValid = password.length >= 6

            when {
                name.isEmpty() -> Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                email.isEmpty() -> Toast.makeText(context, "Ingresa tu email", Toast.LENGTH_SHORT).show()
                !isEmailValid -> Toast.makeText(context, "Email inválido", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(context, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
                !isPasswordValid -> Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                password != confirmPassword -> Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                else -> {
                    viewModel.registerUser(name, email, password)
                }
            }
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LoginColors.GreenBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Botón de volver
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = LoginColors.DarkGreen
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Crear cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = LoginColors.DarkGreen
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Regístrate para comenzar",
                fontSize = 16.sp,
                color = LoginColors.TextPrimary.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Campo de nombre
                    NameField(
                        value = name,
                        onValueChange = onNameChange
                    )

                    Spacer(Modifier.height(12.dp))

                    // Campo de email
                    EmailField(
                        value = email,
                        onValueChange = onEmailChange
                    )

                    Spacer(Modifier.height(12.dp))

                    // Campo de contraseña
                    PasswordField(
                        value = password,
                        onValueChange = onPasswordChange
                    )

                    Spacer(Modifier.height(12.dp))

                    // Campo de confirmar contraseña
                    ConfirmPasswordField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange
                    )

                    Spacer(Modifier.height(24.dp))

                    // Botón de registro
                    DarkGreenButton(
                        text = "Registrarse",
                        onClick = onRegisterClick,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Link para ir a login
                    LoginFooter(onClick = onNavigateBack)
                }
            }
        }

        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun NameField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Nombre completo") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre icon") },
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ConfirmPasswordField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Confirmar contraseña") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password icon") },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (showPassword) androidx.compose.ui.text.input.VisualTransformation.None
        else androidx.compose.ui.text.input.PasswordVisualTransformation()
    )
}

@Composable
fun LoginFooter(onClick: () -> Unit) {
    val greencolor = Color(0xFF467848)
    val text = androidx.compose.ui.text.buildAnnotatedString {
        append("¿Ya tienes cuenta? ")
        withStyle(androidx.compose.ui.text.SpanStyle(color = greencolor, fontWeight = FontWeight.Bold)) {
            append("Inicia sesión")
        }
    }
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        fontSize = 14.sp
    )
}

@Preview(showBackground = true, name = "Registro Normal")
@Composable
fun RegisterPreview() {
    TomateTuTiempoTheme {
        RegisterContent(
            uiState = RegisterUiState(isLoading = false),
            name = "Juan Pérez",
            email = "juan@example.com",
            password = "password123",
            confirmPassword = "password123",
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {},
            onNavigateBack = {}
        )
    }
}