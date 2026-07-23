package com.example.flowpay.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.example.flowpay.R
import com.example.flowpay.components.FlowPayTextField
import com.example.flowpay.RetrofitClient
import com.example.flowpay.LoginRequest
import com.example.flowpay.GoogleLoginRequest
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun LoginScreen(
    registeredEmail: String,
    registeredPassword: String,
    onLoginSuccess: (idUsuario: Int, nombreUsuario: String?, correoUsuario: String?) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mostrarSugerenciaGoogle by remember { mutableStateOf(false) }

    LaunchedEffect(email) {
        val correoLimpio = email.trim()
        if (correoLimpio.contains("@") && correoLimpio.contains(".")) {
            try {
                val respuesta = RetrofitClient.apiService.verificarProveedor(correoLimpio)
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    mostrarSugerenciaGoogle = respuesta.body()?.esGoogle ?: false
                } else {
                    mostrarSugerenciaGoogle = false
                }
            } catch (e: Exception) {
                Log.e("FlowPayTest", "Error consultando proveedor: ${e.message}")
                mostrarSugerenciaGoogle = false
            }
        } else {
            mostrarSugerenciaGoogle = false
        }
    }

    val ejecutarLoginGoogle: suspend () -> Unit = {
        try {
            val credentialManager = CredentialManager.create(context)
            val webClientId = "674577932559-kksvij3am1g84airvmfvitddihd0psit.apps.googleusercontent.com"

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            Log.d("FlowPayTest", "Lanzando ventana de Google...")
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential

            if (credential is GoogleIdTokenCredential) {
                val tokenRealGoogle = credential.idToken
                Log.d("FlowPayTest", "Token de Google obtenido con éxito. Enviando al servidor...")

                val tokenGooglePayload = GoogleLoginRequest(idToken = tokenRealGoogle)
                val respuesta = RetrofitClient.apiService.loginConGoogle(tokenGooglePayload)

                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val body = respuesta.body()
                    val idUsuarioAutenticado = body?.usuario?.id ?: 5
                    val nombreUsuario = body?.usuario?.nombre ?: "Estudiante"
                    val correoUsuario = body?.usuario?.correo ?: ""

                    Log.d("FlowPayTest", "🚀 Login con Google exitoso en Servidor. ID: $idUsuarioAutenticado")
                    Toast.makeText(context, "¡Bienvenido con Google!", Toast.LENGTH_SHORT).show()

                    onLoginSuccess(idUsuarioAutenticado, nombreUsuario, correoUsuario)
                } else {
                    Log.e("FlowPayTest", "El servidor rechazó el Token de Google. Código: ${respuesta.code()}")
                    Toast.makeText(context, "Error al validar la cuenta con el servidor", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("FlowPayTest", "Tipo de credencial no soportado por el dispositivo")
            }
        } catch (e: Exception) {
            Log.e("FlowPayTest", "Fallo el flujo de Google: ${e.localizedMessage}")
            Toast.makeText(context, "Inicio de sesión cancelado o interrumpido", Toast.LENGTH_SHORT).show()
        }
    }

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
            onClick = onNavigateBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 12.dp, top = 8.dp)
                .align(Alignment.TopStart)
                .testTag("login_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar al Landing",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF0F6E36), shape = RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_flowpay),
                    contentDescription = "Logo FlowPay",
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "FlowPay",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Controla tus ganancias de forma sencilla.",
                fontSize = 16.sp,
                color = Color(0xFFF5F5F5),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0x4D112233),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0x33FFFFFF),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AnimatedVisibility(
                    visible = mostrarSugerenciaGoogle,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable {
                                scope.launch { ejecutarLoginGoogle() }
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0x261DB954)),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x661DB954))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(Color.White, shape = RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "G", color = Color(0xFF1DB954), fontStyle = androidx.compose.ui.text.font.FontStyle.Normal, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "¿Eres tú?",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Continúa con tu cuenta de Google",
                                    color = Color(0xFFE0E0E0),
                                    fontSize = 11.sp
                                )
                            }

                            Text(text = "➔", color = Color.White, fontSize = 14.sp)
                        }
                    }
                }

                Text(
                    text = "Correo Electrónico",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    fontWeight = FontWeight.Bold
                )

                FlowPayTextField(
                    value = email,
                    onValueChange = { input ->
                        val isValidChar = input.all { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' || it == '-' }
                        if (input.length <= 30 && isValidChar) email = input
                    },
                    label = "nombre@ejemplo.com",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Contraseña",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    fontWeight = FontWeight.SemiBold
                )

                FlowPayTextField(
                    value = password,
                    onValueChange = { input -> if (input.length <= 20 && input.all { it.isLetterOrDigit() }) password = input },
                    label = "••••••••",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = Color(0xFFF5F5F5),
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 24.dp)
                        .clickable { onNavigateToForgotPassword() }
                )

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            val datosLogin = LoginRequest(email.trim(), password)

                            scope.launch {
                                try {
                                    Log.d("FlowPayTest", "Mandando login a la API... Email: ${email.trim()}")
                                    val respuesta = RetrofitClient.apiService.loginUsuario(datosLogin)

                                    if (respuesta.isSuccessful && respuesta.body() != null) {
                                        Log.d("FlowPayTest", "🚀 Login exitoso. Redireccionando sin crear jornada previa.")

                                        val body = respuesta.body()
                                        val idUsuarioAutenticado = body?.usuario?.id ?: 5
                                        val nombreUsuario = body?.usuario?.nombre ?: "Estudiante"
                                        val correoUsuario = body?.usuario?.correo ?: ""

                                        Toast.makeText(context, "¡Bienvenido de vuelta!", Toast.LENGTH_SHORT).show()

                                        onLoginSuccess(idUsuarioAutenticado, nombreUsuario, correoUsuario)
                                    } else {
                                        Log.e("FlowPayTest", "❌ Rechazado por el backend. Código: ${respuesta.code()}")

                                        try {
                                            val errorBodyString = respuesta.errorBody()?.string()
                                            if (!errorBodyString.isNullOrBlank()) {
                                                val jsonObject = JSONObject(errorBodyString)
                                                val mensajeDelServidor = jsonObject.getString("msg")
                                                Toast.makeText(context, mensajeDelServidor, Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("FlowPayTest", "💥 Fallo de red: ${e.message}")
                                    Toast.makeText(context, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Iniciar Sesión ", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = "➔", fontSize = 16.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "o continuar con", color = Color(0xFFCFD8DC), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        scope.launch { ejecutarLoginGoogle() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0x1AFFFFFF),
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x26FFFFFF))
                ) {
                    Text(text = "G  Continuar con Google", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "¿No tienes una cuenta? ", color = Color(0xFFE0E0E0), fontSize = 13.sp)
                    Text(
                        text = "Crear cuenta",
                        color = Color(0xFF1DB954),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}