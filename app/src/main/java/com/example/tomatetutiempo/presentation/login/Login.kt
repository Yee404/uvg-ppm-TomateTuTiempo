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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.tomatetutiempo.presentation.components.DarkGreenButton
import com.example.tomatetutiempo.presentation.components.EmailField
import com.example.tomatetutiempo.presentation.theme.LoginColors
import com.example.tomatetutiempo.presentation.components.OrDivider
import com.example.tomatetutiempo.presentation.components.PasswordField
import com.example.tomatetutiempo.presentation.components.RegisterFooter
import com.example.tomatetutiempo.presentation.components.RememberForgotRow
import com.example.tomatetutiempo.presentation.components.SocialButton
import com.example.tomatetutiempo.R
import com.example.tomatetutiempo.ui.theme.TomateTuTiempoTheme

@Composable
fun Login() {
    val context = LocalContext.current

    // Estados para los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    // Obtener todos los strings primero
    val logingoogleText = stringResource(R.string.logingoogle)
    val loginfaceText = stringResource(R.string.loginface)
    val forgotpassText = stringResource(R.string.forgotpass)
    val ingresarEmailText = stringResource(R.string.ingresar_email)
    val emailInvalidoText = stringResource(R.string.email_invalido)
    val ingresarContrasennaText = stringResource(R.string.ingresar_contrasenna)
    val contrasennaInvalidaText = stringResource(R.string.contrasenna_invalida)
    val loginExitosoText = stringResource(R.string.login_exitoso)
    val navigateRegisterText = stringResource(R.string.navigate_register)

    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(LoginColors.GreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.Companion.Start) {

            Spacer(modifier = Modifier.Companion.height(20.dp))

            // TITULOS
            Text(
                text = stringResource(R.string.bienvenido_a),
                fontSize = 28.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = LoginColors.TextPrimary
            )
            Spacer(modifier = Modifier.Companion.height(12.dp))
            Text(
                text = stringResource(R.string.tu_tiempo),
                fontSize = 32.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = LoginColors.DarkGreen
            )
            Spacer(modifier = Modifier.Companion.height(32.dp))

            Card(
                modifier = Modifier.Companion.fillMaxWidth(0.92f)
                    .align(Alignment.Companion.CenterHorizontally),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Companion.White)
            ) {
                Column(modifier = Modifier.Companion.padding(16.dp)) {

                    // Botones sociales
                    SocialButton(
                        text = logingoogleText,
                        onClick = {
                            Toast.makeText(context, logingoogleText, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.Companion.fillMaxWidth(),
                        container = LoginColors.LightGreen,
                        content = Color.Companion.Black,
                        border = BorderStroke(1.dp, LoginColors.GrayBorder)
                    )

                    Spacer(Modifier.Companion.height(12.dp))

                    SocialButton(
                        text = loginfaceText,
                        onClick = {
                            Toast.makeText(context, loginfaceText, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.Companion.fillMaxWidth(),
                        container = LoginColors.LightGreen,
                        content = Color.Companion.Black,
                        border = BorderStroke(1.dp, LoginColors.GrayBorder)
                    )

                    Spacer(Modifier.Companion.height(24.dp))
                    OrDivider(
                        text = stringResource(R.string.or),
                        color = LoginColors.GrayBorder,
                        textColor = Color.Companion.Gray
                    )
                    Spacer(Modifier.Companion.height(24.dp))

                    // Campos
                    EmailField(value = email, onValueChange = { email = it })
                    Spacer(Modifier.Companion.height(12.dp))
                    PasswordField(value = password, onValueChange = { password = it })
                    Spacer(Modifier.Companion.height(8.dp))

                    // Remember / Forgot
                    RememberForgotRow(
                        remember = rememberMe,
                        onRememberChange = { rememberMe = it }) {
                        Toast.makeText(context, forgotpassText, Toast.LENGTH_SHORT).show()
                    }

                    Spacer(Modifier.Companion.height(16.dp))

                    // Login Button
                    DarkGreenButton(text = stringResource(R.string.login), onClick = {
                        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        val isPasswordValid = password.length >= 6
                        when {
                            email.isEmpty() -> Toast.makeText(
                                context,
                                ingresarEmailText,
                                Toast.LENGTH_SHORT
                            ).show()

                            !isEmailValid -> Toast.makeText(
                                context,
                                emailInvalidoText,
                                Toast.LENGTH_SHORT
                            ).show()

                            password.isEmpty() -> Toast.makeText(
                                context,
                                ingresarContrasennaText,
                                Toast.LENGTH_SHORT
                            ).show()

                            !isPasswordValid -> Toast.makeText(
                                context,
                                contrasennaInvalidaText,
                                Toast.LENGTH_SHORT
                            ).show()

                            else -> {
                                val msg =
                                    loginExitosoText.replace("\$rememberMe", rememberMe.toString())
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

                    Spacer(Modifier.Companion.height(100.dp))

                    // Footer
                    RegisterFooter {
                        Toast.makeText(context, navigateRegisterText, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TomateTuTiempoTheme {
        Login()
    }
}