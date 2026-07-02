package com.example.flowpay.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("DefaultLocale")
@Composable
fun DayDetailScreen(
    date: String,
    totalSalesStr: String,
    totalInvestmentStr: String,
    netProfit: Double,
    onNavigateBack: () -> Unit,
    onViewReceipts: () -> Unit,
    onDashboardClick: () -> Unit,
    onProductosClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onPerfilClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sales = totalSalesStr.toDoubleOrNull() ?: 0.0
    val investment = totalInvestmentStr.toDoubleOrNull() ?: 0.0

    val profit = sales - investment

    val backgroundColor = Color(0xFF0F172A)
    val primaryGreen = Color(0xFF1DB954)
    val whiteText = Color(0xFFFFFFFF)
    val secondaryText = Color(0xFFD1D5DB)
    val cardBackground = Color(0xFF1E293B)
    val bottomNavBg = Color(0xFF0B1222)

    Box(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(bottom = 84.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = whiteText)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = date, color = whiteText, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Resumen detallado de operaciones", color = secondaryText.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = cardBackground), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Ventas Totales", color = secondaryText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$${String.format("%.2f", sales)}", color = whiteText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = cardBackground), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Inversión", color = secondaryText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$${String.format("%.2f", investment)}", color = whiteText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp).border(1.dp, primaryGreen.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Ganancia Neta", color = primaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("$${String.format("%.2f", profit)}", color = primaryGreen, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(primaryGreen.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ShowChart, contentDescription = null, tint = primaryGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Transacciones", color = whiteText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Ver comprobantes", color = primaryGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onViewReceipts() })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(modifier = Modifier.fillMaxWidth().border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)), colors = CardDefaults.cardColors(containerColor = cardBackground), shape = RoundedCornerShape(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(primaryGreen.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Receipt, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Venta general registrada", color = whiteText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Resumen consolidado del día", color = secondaryText.copy(alpha = 0.7f), fontSize = 12.sp)
                            }
                            Text("+$${String.format("%.2f", sales)}", color = primaryGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    Text(
                        text = "Nota: El desglose exacto por producto se guardará una vez que se conecte la base de datos.",
                        color = secondaryText.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
                    )
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
                .border(1.dp, Color(0xFFFFFFFF).copy(alpha = 0.05f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailBottomNavItem(label = "Dashboard", icon = Icons.Default.GridView, isSelected = false, onClick = onDashboardClick, modifier = Modifier.weight(1f))
                DetailBottomNavItem(label = "Productos", icon = Icons.Default.Inventory2, isSelected = false, onClick = onProductosClick, modifier = Modifier.weight(1f))
                DetailBottomNavItem(label = "Historial", icon = Icons.Default.History, isSelected = true, onClick = onHistorialClick, modifier = Modifier.weight(1f))
                DetailBottomNavItem(label = "Perfil", icon = Icons.Default.Person, isSelected = false, onClick = onPerfilClick, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DetailBottomNavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val primaryGreen = Color(0xFF1DB954)
    val secondaryText = Color(0xFFD1D5DB)
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = icon, contentDescription = label, tint = if (isSelected) primaryGreen else secondaryText.copy(alpha = 0.6f), modifier = Modifier.size(24.dp))
            Text(text = label, color = if (isSelected) primaryGreen else secondaryText.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, maxLines = 1)
        }
    }
}