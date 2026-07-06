package com.example.flowpay.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.R
import com.example.flowpay.components.FlowPayTextField
import com.example.flowpay.RetrofitClient // 👈 IMPORTANTE
import com.example.flowpay.RegisterRequest // 👈 IMPORTANTE
import kotlinx.coroutines.launch // 👈 PARA LLAMADAS ASÍNCRONAS

@Composable
fun RegisterScreen(
    initialName: String = "",
    initialEmail: String = "",
    initialPassword: String = "",
    initialConfirmPassword: String = "",
    initialPrivacyAccepted: Boolean = false,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPrivacyAcceptedChange: (Boolean) -> Unit,
    onAccountCreated: (String, String, String) -> Unit,
    onPrivacyClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope() // 👈 Ámbito para disparar la petición del backend

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_flowpay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { onAccountCreated("", "", "") },
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Regresar",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color(0x4D112233),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0x33FFFFFF),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_flowpay),
                contentDescription = "Logo FlowPay",
                modifier = Modifier
                    .size(75.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Únete y controla tus ganancias diarias.",
                fontSize = 14.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(text = "Nombre Completo", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = initialName,
                onValueChange = { input ->
                    if (input.length <= 50 && input.all { it.isLetter() || it.isWhitespace() }) {
                        onNameChange(input)
                    }
                },
                label = "Ej. Juan Pérez",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(text = "Correo Electrónico", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = initialEmail,
                onValueChange = { input ->
                    val isValidChar = input.all { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' || it == '-' }
                    if (input.length <= 40 && isValidChar) {
                        onEmailChange(input)
                    }
                },
                label = "correo@ejemplo.com",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(text = "Contraseña", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = initialPassword,
                onValueChange = { input ->
                    if (input.length <= 20 && input.all { it.isLetterOrDigit() }) {
                        onPasswordChange(input)
                    }
                },
                label = "Mínimo 8 caracteres",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(text = "Confirmar Contraseña", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = initialConfirmPassword,
                onValueChange = { input ->
                    if (input.length <= 20 && input.all { it.isLetterOrDigit() }) {
                        onConfirmPasswordChange(input)
                    }
                },
                label = "Repite tu contraseña",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = initialPrivacyAccepted,
                    onCheckedChange = { onPrivacyAcceptedChange(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF1DB954),
                        uncheckedColor = Color.LightGray,
                        checkmarkColor = Color.White
                    )
                )

                Text(
                    text = "He leído y acepto el ",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Text(
                    text = "Aviso de Privacidad.",
                    color = Color(0xFF1DB954),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onPrivacyClick() }
                )
            }

            // 🟢 BOTÓN DE REGISTRO CON CONEXIÓN AL BACKEND AÑADIDO
            Button(
                onClick = {
                    if (initialName.isNotBlank() && initialEmail.isNotBlank() && initialPassword.isNotBlank()) {
                        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(initialEmail.trim()).matches()

                        if (!isEmailValid) {
                            Toast.makeText(context, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
                        } else if (initialPrivacyAccepted) {
                            if (initialPassword.length >= 8) {
                                if (initialPassword == initialConfirmPassword) {

                                    // 🚀 AQUÍ EMPIEZA LA CONEXIÓN REAL AL SERVIDOR
                                    val datosRegistro = RegisterRequest(
                                        nombre = initialName.trim(),
                                        correo = initialEmail.trim(),
                                        contrasena = initialPassword
                                    )

                                    scope.launch {
                                        try {
                                            Log.d("FlowPayTest", "Enviando registro al backend...")
                                            val respuesta = RetrofitClient.apiService.registrarUsuario(datosRegistro)

                                            if (respuesta.isSuccessful) {
                                                Log.d("FlowPayTest", "✅ Registrado con éxito en la Base de Datos")
                                                Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()

                                                // Te manda al login pasándole los datos reales
                                                onAccountCreated(initialName, initialEmail, initialPassword)
                                            } else {
                                                val errorString = respuesta.errorBody()?.string()
                                                Log.e("FlowPayTest", "❌ Error en Registro: $errorString")
                                                Toast.makeText(context, "Error al registrar: Servidor lo rechazó", Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            Log.e("FlowPayTest", "💥 Fallo de red en registro: ${e.message}")
                                            Toast.makeText(context, "Error de red con el servidor", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                } else {
                                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "La contraseña debe tener mínimo 8 caracteres", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Debes aceptar el Aviso de Privacidad", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Registrarme", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "¿Ya tienes una cuenta? ", color = Color.LightGray, fontSize = 13.sp)
                Text(
                    text = "Iniciar sesión",
                    color = Color(0xFF1DB954),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onAccountCreated("", "", "") }
                )
            }
        }
    }
}