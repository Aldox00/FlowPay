package com.example.flowpay.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.DailyRecord

@Composable
fun HistoryScreen(
    records: List<DailyRecord>,
    onNavigateToDashboard: () -> Unit
) {
    val backgroundColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val greenColor = Color(0xFF1DB954)
    val inactiveGray = Color.Gray

    Scaffold(
        bottomBar = {
            Surface(
                color = Color(0xFF0B1222),
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onNavigateToDashboard() }.padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.GridView, contentDescription = null, tint = inactiveGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Dashboard", fontSize = 11.sp, color = inactiveGray)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }.padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, tint = inactiveGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Productos", fontSize = 11.sp, color = inactiveGray)
                    }

                    Surface(
                        color = greenColor,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(40.dp).width(90.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Historial", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }.padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = inactiveGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Perfil", fontSize = 11.sp, color = inactiveGray)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "FlowPay",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = greenColor,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF151F32))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 20.dp)
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val days = listOf(
                        "LUN" to 0.5f,
                        "MAR" to 0.8f,
                        "MIE" to 0.4f,
                        "JUE" to 1.0f,
                        "VIE" to 0.7f,
                        "SAB" to 0.85f,
                        "DOM" to 1.0f
                    )

                    days.forEach { (name, weight) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(weight)
                                        .background(
                                            color = if (name == "DOM") greenColor else Color(0xFF24344D),
                                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                        )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = name,
                                fontSize = 11.sp,
                                color = if (name == "DOM") greenColor else inactiveGray,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Text(
                text = "Registros Recientes",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            records.forEach { record ->
                HistoryDayCard(
                    date = record.date,
                    sales = "$${String.format("%.2f", record.sales)}",
                    investment = "$${String.format("%.2f", record.investment)}",
                    profit = "$${String.format("%.2f", record.profit)}",
                    cardColor = cardColor,
                    greenColor = greenColor
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun HistoryDayCard(
    date: String,
    sales: String,
    investment: String,
    profit: String,
    cardColor: Color,
    greenColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = date, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Column {
                        Text(text = "VENTAS", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(text = sales, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text(text = "INVERSIÓN", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(text = investment, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "GANANCIA", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(text = profit, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = greenColor)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.DarkGray)
            }
        }
    }
}