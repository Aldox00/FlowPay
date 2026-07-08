package com.example.flowpay.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.example.flowpay.RetrofitClient
import com.example.flowpay.SolicitarRecuperacionRequest
import com.example.flowpay.VerificarCodigoRequest
import com.example.flowpay.RestablecerPasswordRequest
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class RecoveryStep {
    INGRESAR_CORREO,
    VERIFICAR_CODIGO,
    RESTABLECER_FORMULARIO
}

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRestablecer: (String) -> Unit
) {
    var currentStep by remember { mutableStateOf(RecoveryStep.INGRESAR_CORREO) }
    var email by remember { mutableStateOf("") }
    var codigoVerificacion by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var tokenSesionTemporal by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_flowpay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (currentStep == RecoveryStep.VERIFICAR_CODIGO) currentStep = RecoveryStep.INGRESAR_CORREO
                    else if (currentStep == RecoveryStep.RESTABLECER_FORMULARIO) currentStep = RecoveryStep.VERIFICAR_CODIGO
                    else onNavigateBack()
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Regresar",
                    tint = Color(0xFF1DB954)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "FlowPay",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1DB954),
                modifier = Modifier.padding(end = 40.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
                .background(color = Color(0xED111A2E), shape = RoundedCornerShape(24.dp))
                .border(width = 1.dp, color = Color(0x1AFFFFFF), shape = RoundedCornerShape(24.dp))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(65.dp)
                    .background(Color(0x1A1DB954), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when(currentStep) {
                    RecoveryStep.INGRESAR_CORREO -> "🔄"
                    RecoveryStep.VERIFICAR_CODIGO -> "🔢"
                    RecoveryStep.RESTABLECER_FORMULARIO -> "🔒"
                }
                Text(text = emoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (currentStep) {
                RecoveryStep.INGRESAR_CORREO -> {
                    Text(
                        text = "Recuperar\ncontraseña",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Te enviaremos un código de seguridad de 6 dígitos para restablecer tu acceso.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = "CORREO ELECTRÓNICO",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )
                    FlowPayTextField(
                        value = email,
                        onValueChange = { input ->
                            val isValidChar = input.all { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' || it == '-' }
                            if (input.length <= 30 && isValidChar) email = input
                        },
                        label = "nombre@ejemplo.com",
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Button(
                        onClick = {
                            if (email.isNotBlank() && email.contains("@") && email.contains(".")) {
                                scope.launch {
                                    try {
                                        val datosEnvio = SolicitarRecuperacionRequest(email.trim())
                                        val respuesta = RetrofitClient.apiService.solicitarRecuperacion(datosEnvio)

                                        if (respuesta.isSuccessful && respuesta.body() != null) {
                                            val mensajeServer = respuesta.body()?.msg ?: "Código enviado."
                                            Toast.makeText(context, mensajeServer, Toast.LENGTH_LONG).show()
                                            currentStep = RecoveryStep.VERIFICAR_CODIGO
                                        } else {
                                            val errorBodyString = respuesta.errorBody()?.string()
                                            val msg = if (!errorBodyString.isNullOrBlank()) JSONObject(errorBodyString).getString("msg") else "El correo no está registrado"
                                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Introduce un correo válido", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(text = "Enviar código de seguridad ➔", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                RecoveryStep.VERIFICAR_CODIGO -> {
                    Text(
                        text = "Introduce el\ncódigo",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Introduce el código de 6 dígitos que enviamos a tu buzón.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = "CÓDIGO DE VERIFICACIÓN",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )
                    FlowPayTextField(
                        value = codigoVerificacion,
                        onValueChange = { input ->
                            if (input.length <= 6 && input.all { it.isDigit() }) codigoVerificacion = input
                        },
                        label = "000000",
                        leadingIcon = Icons.Default.Lock,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Button(
                        onClick = {
                            if (codigoVerificacion.length == 6) {
                                scope.launch {
                                    try {
                                        val datosVerificar = VerificarCodigoRequest(email.trim(), codigoVerificacion.trim())
                                        val respuesta = RetrofitClient.apiService.verificarCodigo(datosVerificar)

                                        if (respuesta.isSuccessful && respuesta.body() != null) {
                                            tokenSesionTemporal = respuesta.body()?.token ?: ""
                                            Toast.makeText(context, "Identidad confirmada", Toast.LENGTH_SHORT).show()
                                            currentStep = RecoveryStep.RESTABLECER_FORMULARIO
                                        } else {
                                            val errorBodyString = respuesta.errorBody()?.string()
                                            val msg = if (!errorBodyString.isNullOrBlank()) JSONObject(errorBodyString).getString("msg") else "Código incorrecto"
                                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error al validar código", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "El código debe ser de 6 dígitos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(text = "Verificar identidad ➔", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                RecoveryStep.RESTABLECER_FORMULARIO -> {
                    Text(
                        text = "Nueva contraseña",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Introduce tu nueva contraseña de acceso para FlowPay.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "NUEVA CONTRASEÑA",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )

                    FlowPayTextField(
                        value = nuevaContrasena,
                        onValueChange = { input ->
                            val textoLimpio = input.filter { it.isLetterOrDigit() }
                            nuevaContrasena = textoLimpio.take(16)
                        },
                        label = "••••••••",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        isError = nuevaContrasena.isNotEmpty() && nuevaContrasena.length < 6,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "CONFIRMAR NUEVA CONTRASEÑA",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )

                    FlowPayTextField(
                        value = confirmarContrasena,
                        onValueChange = { input ->
                            val textoLimpio = input.filter { it.isLetterOrDigit() }
                            confirmarContrasena = textoLimpio.take(16)
                        },
                        label = "••••••••",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        isError = confirmarContrasena.isNotEmpty() && confirmarContrasena != nuevaContrasena,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Button(
                        onClick = {
                            if (nuevaContrasena.isNotBlank() && nuevaContrasena == confirmarContrasena) {
                                if (nuevaContrasena.length < 6) {
                                    Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                scope.launch {
                                    try {
                                        val datosActualizar = RestablecerPasswordRequest(tokenSesionTemporal, nuevaContrasena.trim())
                                        val respuesta = RetrofitClient.apiService.restablecerContrasena(datosActualizar)

                                        if (respuesta.isSuccessful) {
                                            val msgSuccess = respuesta.body()?.msg ?: "Contraseña actualizada"
                                            Toast.makeText(context, msgSuccess, Toast.LENGTH_LONG).show()
                                            onNavigateBack()
                                        } else {
                                            Toast.makeText(context, "No se pudo actualizar. Token inválido o no verificado.", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error en la conexión final", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(text = "Actualizar contraseña ➔", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "¿Recordaste tu contraseña? ", color = Color.Gray, fontSize = 13.sp)
            Text(
                text = "Iniciar sesión",
                color = Color(0xFF1DB954),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onNavigateBack() }
            )
        }
    }
}