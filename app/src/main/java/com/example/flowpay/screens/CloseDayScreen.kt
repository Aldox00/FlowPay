package com.example.flowpay.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.EncuestaRequest
import com.example.flowpay.RetrofitClient
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CloseDayScreen(
    jornadaId: Int,
    usuarioId: Int = 0,
    totalSales: Double,
    totalInvestment: Double,
    netProfit: Double,
    onNavigateBack: () -> Unit,
    onFinalizeDay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var puntuacionApp by remember { mutableIntStateOf(5) }
    var comentariosApp by remember { mutableStateOf("Jornada cerrada exitosamente") }

    val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

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
                    colors = listOf(primaryGreen.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.15f),
                    radius = width * 0.5f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x38, 0xBD, 0xF8).copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.8f, height * 0.8f),
                    radius = width * 0.6f
                )
            )

            val gridSpacing = 40.dp.toPx()
            val gridColor = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.025f)

            var x = 0f
            while (x < width) {
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1.dp.toPx()
                )
                x += gridSpacing
            }

            var y = 0f
            while (y < height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
                y += gridSpacing
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retroceder",
                        tint = whiteText,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(primaryGreen.copy(alpha = 0.15f))
                    .border(2.dp, primaryGreen, CircleShape)
                    .testTag("checkmark_badge"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completado",
                    tint = primaryGreen,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Cierre de Caja",
                color = whiteText,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Jornada finalizada con éxito",
                color = secondaryText.copy(alpha = 0.8f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CloseDayCard(
                    label = "Total Vendido",
                    value = format.format(totalSales),
                    valueColor = whiteText,
                    icon = Icons.Default.Payments,
                    cardBgColor = cardBackground,
                    iconTint = Color(0x9C, 0xA3, 0xAF),
                    modifier = Modifier.testTag("card_total_vendido")
                )

                CloseDayCard(
                    label = "Inversión del día",
                    value = format.format(totalInvestment),
                    valueColor = whiteText,
                    icon = Icons.Default.Wallet,
                    cardBgColor = cardBackground,
                    iconTint = Color(0x9C, 0xA3, 0xAF),
                    modifier = Modifier.testTag("card_inversion_dia")
                )

                CloseDayCard(
                    label = "Ganancia Neta",
                    value = format.format(netProfit),
                    valueColor = primaryGreen,
                    icon = Icons.Default.TrendingUp,
                    cardBgColor = cardBackground,
                    iconTint = primaryGreen,
                    isTall = true,
                    modifier = Modifier.testTag("card_ganancia_neta")
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val encuestaRequest = EncuestaRequest(
                                jornada_id = jornadaId,
                                id_usuario = usuarioId,
                                pregunta_1 = puntuacionApp,
                                pregunta_2 = puntuacionApp,
                                pregunta_3 = puntuacionApp,
                                puntuacion_app = puntuacionApp,
                                comentarios = comentariosApp
                            )

                            val response = RetrofitClient.apiService.registrarEncuesta(encuestaRequest)

                            if (response.isSuccessful && response.body()?.ok == true) {
                                Log.d("FlowPayTest", "✅ Encuesta guardada con éxito en la BD")
                            } else {
                                Log.e("FlowPayTest", "❌ El servidor rechazó la encuesta: ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            Log.e("FlowPayTest", "❌ Error de red al guardar encuesta: ${e.message}")
                        } finally {
                            onFinalizeDay()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    contentColor = Color(0x0F, 0x17, 0x2A)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("finalizar_jornada_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Finalizar Jornada",
                        color = Color(0x0F, 0x17, 0x2A),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color(0x0F, 0x17, 0x2A),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CloseDayCard(
    label: String,
    value: String,
    valueColor: Color,
    icon: ImageVector,
    cardBgColor: Color,
    iconTint: Color,
    isTall: Boolean = false,
    modifier: Modifier = Modifier
) {
    val verticalPadding = if (isTall) 24.dp else 18.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .border(
                width = 1.dp,
                color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp, vertical = verticalPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = label,
                    color = Color(0xD1, 0xD5, 0xDB).copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = value,
                    color = valueColor,
                    fontSize = if (isTall) 32.sp else 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = if (isTall) 0.5.sp else 0.sp
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(if (isTall) 32.dp else 24.dp)
            )
        }
    }
}