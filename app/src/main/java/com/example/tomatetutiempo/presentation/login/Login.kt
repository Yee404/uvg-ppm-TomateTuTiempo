package com.example.tomatetutiempo.presentation.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.presentation.components.*
import com.example.tomatetutiempo.presentation.theme.LoginColors
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme

@Composable
fun Login(
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = uiState) {
        if (uiState.loginSuccess) {
            Toast.makeText(context, "¡Login exitoso!", Toast.LENGTH_SHORT).show()
            onLoginSuccess()
        }
        uiState.error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LoginContent(
        uiState = uiState,
        email = email,
        password = password,
        rememberMe = rememberMe,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onRememberChange = { rememberMe = it },
        onLoginClick = {
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isPasswordValid = password.length >= 6
            when {
                email.isEmpty() -> Toast.makeText(context, "Ingresa tu email", Toast.LENGTH_SHORT).show()
                !isEmailValid -> Toast.makeText(context, "Email inválido", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(context, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
                !isPasswordValid -> Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                else -> {
                    viewModel.loginUser(email, password)
                }
            }
        },
        onSocialGoogleClick = { Toast.makeText(context, "Función no implementada", Toast.LENGTH_SHORT).show() },
        onSocialFacebookClick = { Toast.makeText(context, "Función no implementada", Toast.LENGTH_SHORT).show() },
        onForgotClick = { Toast.makeText(context, "Contraseña olvidada", Toast.LENGTH_SHORT).show() },
        onRegisterClick = { Toast.makeText(context, "Navegar a Registro", Toast.LENGTH_SHORT).show() }
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    email: String,
    password: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onSocialGoogleClick: () -> Unit,
    onSocialFacebookClick: () -> Unit,
    onForgotClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val logingoogleText = stringResource(R.string.logingoogle)
    val loginfaceText = stringResource(R.string.loginface)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LoginColors.GreenBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = stringResource(R.string.bienvenido_a), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = LoginColors.TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.tu_tiempo), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = LoginColors.DarkGreen)
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(0.92f).align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SocialButton(text = logingoogleText, onClick = onSocialGoogleClick, modifier = Modifier.fillMaxWidth(), container = LoginColors.LightGreen, content = Color.Black, border = BorderStroke(1.dp, LoginColors.GrayBorder))
                    Spacer(Modifier.height(12.dp))
                    SocialButton(text = loginfaceText, onClick = onSocialFacebookClick, modifier = Modifier.fillMaxWidth(), container = LoginColors.LightGreen, content = Color.Black, border = BorderStroke(1.dp, LoginColors.GrayBorder))
                    Spacer(Modifier.height(24.dp))
                    OrDivider(text = stringResource(R.string.or), color = LoginColors.GrayBorder, textColor = Color.Gray)
                    Spacer(Modifier.height(24.dp))
                    EmailField(value = email, onValueChange = onEmailChange)
                    Spacer(Modifier.height(12.dp))
                    PasswordField(value = password, onValueChange = onPasswordChange)
                    Spacer(Modifier.height(8.dp))
                    // AQUÍ ESTABA EL ERROR. AHORA ESTÁ CORREGIDO.
                    RememberForgotRow(remember = rememberMe, onRememberChange = onRememberChange, onForgot = onForgotClick)
                    Spacer(Modifier.height(16.dp))
                    DarkGreenButton(text = stringResource(R.string.login), onClick = onLoginClick)
                    Spacer(Modifier.height(100.dp))
                    RegisterFooter(onClick = onRegisterClick)
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true, name = "Estado Normal")
@Composable
fun LoginPreview() {
    TomateTuTiempoTheme {
        LoginContent(
            uiState = LoginUiState(isLoading = false),
            email = "test@correo.com",
            password = "password",
            rememberMe = true,
            onEmailChange = {},
            onPasswordChange = {},
            onRememberChange = {},
            onLoginClick = {},
            onSocialGoogleClick = {},
            onSocialFacebookClick = {},
            onForgotClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado de Carga")
@Composable
fun LoginLoadingPreview() {
    TomateTuTiempoTheme {
        LoginContent(
            uiState = LoginUiState(isLoading = true),
            email = "test@correo.com",
            password = "password",
            rememberMe = true,
            onEmailChange = {},
            onPasswordChange = {},
            onRememberChange = {},
            onLoginClick = {},
            onSocialGoogleClick = {},
            onSocialFacebookClick = {},
            onForgotClick = {},
            onRegisterClick = {}
        )
    }
}