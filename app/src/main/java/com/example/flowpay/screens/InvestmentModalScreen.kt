package com.example.flowpay.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.flowpay.RetrofitClient
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch

data class JornadaRequest(
    val usuario_id: Int,
    @SerializedName("monto_inversion")
    val inversion_inicial: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentModalScreen(
    usuarioIdActivo: Int,
    onSaveAndStart: (investmentAmount: Double, jornadaId: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val cardBackground = Color(0x1E, 0x29, 0x3B)
    val inputBackground = Color(0x0F, 0x17, 0x2A)
    val secondaryText = Color(0x9C, 0xA3, 0xAF)

    var investmentValue by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(cardBackground)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(primaryGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Inversión",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "¿Cuánto invertiste hoy?",
                    color = whiteText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Registra lo que gastaste en ingredientes antes de empezar a vender. Tus inversiones te ayudan a crecer.",
                    color = secondaryText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Inversión Diaria",
                        color = primaryGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextField(
                        value = investmentValue,
                        onValueChange = { investmentValue = it },
                        placeholder = {
                            Text(text = "0.00", color = secondaryText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        },
                        leadingIcon = {
                            Text(text = "$", color = primaryGreen, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        },
                        trailingIcon = {
                            Text(text = "MXN", color = secondaryText, fontWeight = FontWeight.Bold)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = inputBackground,
                            unfocusedContainerColor = inputBackground,
                            focusedTextColor = whiteText,
                            unfocusedTextColor = whiteText,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val isButtonEnabled = investmentValue.isNotBlank() && !isSaving
                Button(
                    onClick = {
                        val cleanInput = investmentValue.replace(",", ".").replace(" ", "").trim()
                        val amount = cleanInput.toDoubleOrNull() ?: 0.0

                        if (amount >= 0) {
                            isSaving = true

                            scope.launch {
                                try {
                                    Log.d("FlowPayTest", "Iniciando jornada en la API para usuario $usuarioIdActivo con monto: $amount...")

                                    val req = JornadaRequest(
                                        usuario_id = usuarioIdActivo,
                                        inversion_inicial = amount
                                    )

                                    var respuesta = RetrofitClient.apiService.abrirJornada(req)
                                    var idJornadaFinal = 0

                                    if (respuesta.isSuccessful) {
                                        idJornadaFinal = respuesta.body()?.jornada_id ?: 0
                                        Log.d("FlowPayTest", "✅ Primera respuesta exitosa de Node.js. ID recibido: $idJornadaFinal")

                                        if (idJornadaFinal == 0) {
                                            Log.d("FlowPayTest", "⚠️ ID en 0 detectado. Activando bypass para extraer ID real de la jornada...")
                                            val respuestaBypass = RetrofitClient.apiService.abrirJornada(req)

                                            if (!respuestaBypass.isSuccessful) {
                                                val errorMsgBypass = respuestaBypass.errorBody()?.string() ?: ""
                                                val match = Regex("\"jornadaId\":\\s*(\\d+)").find(errorMsgBypass)
                                                idJornadaFinal = match?.groups?.get(1)?.value?.toIntOrNull() ?: 0
                                                Log.d("FlowPayTest", "🔄 [Bypass Exitoso] ID Real recuperado dinámicamente: $idJornadaFinal")
                                            }
                                        }

                                        Toast.makeText(context, "¡Jornada iniciada con éxito!", Toast.LENGTH_SHORT).show()
                                        onSaveAndStart(amount, idJornadaFinal)

                                    } else {
                                        val errorMsg = respuesta.errorBody()?.string() ?: ""
                                        Log.e("FlowPayTest", "❌ El servidor rechazó la jornada: $errorMsg")

                                        if (errorMsg.contains("ya tienes una activa") || respuesta.code() == 400) {
                                            val match = Regex("\"jornadaId\":\\s*(\\d+)").find(errorMsg)
                                            idJornadaFinal = match?.groups?.get(1)?.value?.toIntOrNull() ?: 0

                                            Log.d("FlowPayTest", "🔄 Recuperado dinámicamente ID de jornada activa existente: $idJornadaFinal")
                                            Toast.makeText(context, "Continuando jornada activa...", Toast.LENGTH_SHORT).show()

                                            onSaveAndStart(amount, idJornadaFinal)
                                        } else {
                                            Toast.makeText(context, "Servidor rechazó el inicio de jornada", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("FlowPayTest", "💥 Fallo crítico de red en Jornada: ${e.message}")
                                    Toast.makeText(context, "Error de red al conectar con el servidor", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    },
                    enabled = isButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        disabledContainerColor = primaryGreen.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isSaving) "Iniciando..." else "Guardar e iniciar jornada",
                            color = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (!isSaving) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}