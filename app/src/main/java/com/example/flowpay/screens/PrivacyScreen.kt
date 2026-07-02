package com.example.flowpay.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryGreen.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.1f),
                    radius = width * 0.7f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.offset(x = (-12).dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = primaryGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Aviso de Privacidad",
                    color = whiteText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardBackground)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Policy,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(48.dp)
                    )

                    Text(
                        text = "Última actualización: Junio de 2026",
                        color = secondaryText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "1. Información que recopilamos",
                        color = whiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "En FlowPay recopilamos información personal que nos proporcionas directamente, como tu nombre completo, dirección de correo electrónico y datos relacionados con las transacciones financieras (ventas e inversiones) de tu emprendimiento estudiantil.",
                        color = secondaryText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = "2. Uso de la información",
                        color = whiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Utilizamos tus datos exclusivamente para proporcionarte el servicio de gestión de ingresos, generar tu historial de ventas diario y ofrecerte herramientas de análisis. No compartimos, vendemos ni alquilamos tus datos financieros a terceros bajo ninguna circunstancia.",
                        color = secondaryText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = "3. Seguridad de tus datos",
                        color = whiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Implementamos medidas de seguridad técnicas y organizativas para proteger tu información contra acceso no autorizado, pérdida o alteración. Todos tus registros de ventas y comprobantes están encriptados en nuestros servidores.",
                        color = secondaryText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}