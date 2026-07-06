package com.example.flowpay.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.example.flowpay.RestablecerPasswordRequest
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun RestablecerPasswordScreen(
    tokenRecuperacion: String, // 🟢 Atrapa el token temporal que viene desde ForgotPasswordScreen
    onPasswordChangedSuccess: () -> Unit // 🟢 Nos regresa al Login al terminar con éxito
) {
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_flowpay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nueva Contraseña",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Crea una nueva contraseña segura para tu cuenta de FlowPay.",
                fontSize = 13.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0x4D112233), shape = RoundedCornerShape(24.dp))
                    .border(width = 1.dp, color = Color(0x33FFFFFF), shape = RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nueva Contraseña",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )

                FlowPayTextField(
                    value = nuevaContrasena,
                    onValueChange = { input -> if (input.length <= 20 && input.all { it.isLetterOrDigit() }) nuevaContrasena = input },
                    label = "Mínimo 6 caracteres",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Confirmar Contraseña",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )

                FlowPayTextField(
                    value = confirmarContrasena,
                    onValueChange = { input -> if (input.length <= 20 && input.all { it.isLetterOrDigit() }) confirmarContrasena = input },
                    label = "Repite tu contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Button(
                    onClick = {
                        if (nuevaContrasena.isNotBlank() && confirmarContrasena.isNotBlank()) {
                            if (nuevaContrasena.length < 6) {
                                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (nuevaContrasena != confirmarContrasena) {
                                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val datosEnvio = RestablecerPasswordRequest(tokenRecuperacion, nuevaContrasena)

                            scope.launch {
                                try {
                                    Log.d("FlowPayTest", "Mandando cambio de password con token...")
                                    val respuesta = RetrofitClient.apiService.restablecerContrasena(datosEnvio)

                                    if (respuesta.isSuccessful) {
                                        Toast.makeText(context, "¡Contraseña actualizada con éxito!", Toast.LENGTH_LONG).show()
                                        onPasswordChangedSuccess() // Desencadena volver al login
                                    } else {
                                        val errorBodyString = respuesta.errorBody()?.string()
                                        val msg = if (!errorBodyString.isNullOrBlank()) {
                                            JSONObject(errorBodyString).getString("msg")
                                        } else { "El token expiró o es inválido" }
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = "Actualizar Contraseña", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}