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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.DailyRecord

@Composable
fun HistoryScreen(
    records: List<DailyRecord>,
    onNavigateToDashboard: () -> Unit,
    onNavigateToProductos: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToSurvey: (String) -> Unit,
    onNavigateToDayDetail: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable { mutableStateOf("Día") }

    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val bottomNavBg = Color(0x0B, 0x0F, 0x19)

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

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 84.dp)) {

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
                                onNavigateToSurvey(filter)
                                selectedFilter = filter
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

            val recordsToShow = when (selectedFilter) {
                "Semana" -> if (records.size >= 7) records.take(7) else emptyList()
                "Mes" -> if (records.size >= 30) records.take(30) else emptyList()
                else -> records
            }

            if (recordsToShow.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "📊", fontSize = 56.sp, modifier = Modifier.padding(bottom = 16.dp))
                    Text(
                        text = "Aún no hay datos suficientes",
                        color = whiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sigue registrando ventas para ver\ntu progreso por ${selectedFilter.lowercase()}.",
                        color = secondaryText,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                val totalGanancia = recordsToShow.sumOf { it.profit }

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (selectedFilter != "Día") {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBackground),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "ESTA ${selectedFilter.uppercase()}", color = secondaryText.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "+$${totalGanancia}", color = primaryGreen, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth().height(120.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        val heights = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 0.3f, 1.0f)
                                        val days = listOf("LUN", "MAR", "MIE", "JUE", "VIE", "SAB", "DOM")
                                        heights.zip(days).forEachIndexed { index, (heightWeight, day) ->
                                            val isToday = index == 6
                                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.weight(1f)) {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(heightWeight).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)).background(if (isToday) primaryGreen else primaryGreen.copy(alpha = 0.3f))
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(text = day, color = if (isToday) primaryGreen else secondaryText.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "Registros Recientes", color = whiteText, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                    }

                    items(recordsToShow) { record ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                                .clickable { onNavigateToDayDetail(record.date, record.sales.toString(), record.investment.toString(), record.profit.toString()) },
                            colors = CardDefaults.cardColors(containerColor = cardBackground),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = record.date, color = whiteText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Column {
                                            Text(text = "VENTAS", color = secondaryText.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "$${record.sales}", color = whiteText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Column {
                                            Text(text = "INVERSIÓN", color = secondaryText.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "$${record.investment}", color = whiteText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "GANANCIA", color = secondaryText.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "$${record.profit}", color = primaryGreen, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = secondaryText.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(84.dp)
                .background(bottomNavBg)
                .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HistoryBottomNavItem(label = "Dashboard", icon = Icons.Default.GridView, isSelected = false, onClick = onNavigateToDashboard, modifier = Modifier.weight(1f))
                HistoryBottomNavItem(label = "Productos", icon = Icons.Default.Inventory2, isSelected = false, onClick = onNavigateToProductos, modifier = Modifier.weight(1f))
                HistoryBottomNavItem(label = "Historial", icon = Icons.Default.History, isSelected = true, onClick = { }, modifier = Modifier.weight(1f))
                HistoryBottomNavItem(label = "Perfil", icon = Icons.Default.Person, isSelected = false, onClick = onNavigateToPerfil, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HistoryBottomNavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) primaryGreen else secondaryText.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                color = if (isSelected) primaryGreen else secondaryText.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}