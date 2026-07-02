package com.example.flowpay.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.DailyRecord

@Composable
fun HistoryScreen(
    records: List<com.example.flowpay.DailyRecord>,
    onNavigateToDashboard: () -> Unit,
    onNavigateToSurvey: (String) -> Unit,
    onNavigateToDayDetail: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Día") }
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)

    Box(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryGreen.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.2f),
                    radius = size.width * 0.6f
                )
            )
        }

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                IconButton(onClick = onNavigateToDashboard, modifier = Modifier.offset(x = (-12).dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = whiteText)
                }
                Text("Historial de Ventas", color = whiteText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("Día", "Semana", "Mes").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) primaryGreen else Color.White.copy(alpha = 0.1f))
                            .clickable {
                                if (filter == "Semana" || filter == "Mes") {
                                    onNavigateToSurvey(filter)
                                } else {
                                    selectedFilter = filter
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color(0x0F, 0x17, 0x2A) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(records) { record ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = record.date, color = whiteText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Inversión: $${record.investment}", color = secondaryText, fontSize = 13.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "+$${record.sales}", color = primaryGreen, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Ganancia: $${record.profit}", color = primaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}