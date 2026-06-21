package com.example.flowpay.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SurveyScreen(onSurveySubmitted: () -> Unit) {
    val context = LocalContext.current

    val darkBgColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val greenColor = Color(0xFF1DB954)

    var ratingQ1 by remember { mutableStateOf(0) }
    var ratingQ2 by remember { mutableStateOf(0) }
    var ratingQ3 by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF064E3B), darkBgColor),
                    endY = 600f
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "FlowPay",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Antes de continuar...",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tu opinión nos ayuda a mejorar FlowPay.",
                fontSize = 14.sp,
                color = greenColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 28.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    SurveyQuestionItem(
                        question = "¿Qué tan fácil y rápido fue registrar tus ventas hoy con FlowPay?",
                        currentRating = ratingQ1,
                        labelMin = "1 Muy difícil",
                        labelMax = "5 Súper fácil",
                        onRatingSelected = { ratingQ1 = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    SurveyQuestionItem(
                        question = "¿El cálculo automático de tu ganancia neta te ayudó a entender mejor tus finanzas del día?",
                        currentRating = ratingQ2,
                        labelMin = "1 Para nada",
                        labelMax = "5 Muchísimo",
                        onRatingSelected = { ratingQ2 = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    SurveyQuestionItem(
                        question = "En general, ¿qué tan satisfecho estás con el rendimiento de la aplicación el día de hoy?",
                        currentRating = ratingQ3,
                        labelMin = "1 Insatisfecho",
                        labelMax = "5 Excelente servicio",
                        onRatingSelected = { ratingQ3 = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (ratingQ1 > 0 && ratingQ2 > 0 && ratingQ3 > 0) {
                        Toast.makeText(context, "¡Gracias por tus respuestas!", Toast.LENGTH_SHORT).show()
                        onSurveySubmitted()
                    } else {
                        Toast.makeText(context, "Por favor responde todas las preguntas", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Enviar y ver mi historial ",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Completar esta encuesta desbloquea tu historial semanal.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            )
        }
    }
}

@Composable
fun SurveyQuestionItem(
    question: String,
    currentRating: Int,
    labelMin: String,
    labelMax: String,
    onRatingSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = question,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 1..5) {
                val isSelected = i <= currentRating
                Icon(
                    imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Calificar $i",
                    tint = if (isSelected) Color(0xFFF59E0B) else Color.DarkGray,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onRatingSelected(i) }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = labelMin, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(text = labelMax, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}