package com.example.flowpay.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentModalScreen(
    onSaveAndStart: (investmentAmount: Double) -> Unit,
    onDismiss: () -> Unit
) {
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val cardBackground = Color(0x1E, 0x29, 0x3B)
    val inputBackground = Color(0x0F, 0x17, 0x2A)
    val secondaryText = Color(0x9C, 0xA3, 0xAF)

    var investmentValue by remember { mutableStateOf("") }

    // Dialog crea la ventana flotante con fondo oscurecido
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
                // Ícono superior
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

                // Títulos
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

                // Input de Inversión
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

                // Botón Principal
                val isButtonEnabled = investmentValue.isNotBlank()
                Button(
                    onClick = {
                        val amount = investmentValue.toDoubleOrNull() ?: 0.0
                        onSaveAndStart(amount)
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
                            text = "Guardar e iniciar jornada",
                            color = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
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