package com.example.flowpay.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DayDetailScreen(
    date: String,
    totalSales: Double,
    totalInvestment: Double,
    netProfit: Double,
    onNavigateBack: () -> Unit,
    onViewReceipts: () -> Unit,
    onDashboardClick: () -> Unit,
    onProductosClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onPerfilClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)
    val bottomNavBg = Color(0x0B, 0x0F, 0x19)

    val scrollState = rememberScrollState()
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
                    center = Offset(width * 0.2f, height * 0.2f),
                    radius = width * 0.5f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x38, 0xBD, 0xF8).copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.8f, height * 0.6f),
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
                .padding(bottom = 84.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = date, // Fecha dinámica
                        color = whiteText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Resumen detallado de operaciones",
                        color = secondaryText.copy(alpha = 0.8f),
                        fontSize = 15.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Ventas Totales",
                                color = secondaryText.copy(alpha = 0.6f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = format.format(totalSales),
                                color = whiteText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Inversión",
                                color = secondaryText.copy(alpha = 0.6f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = format.format(totalInvestment),
                                color = whiteText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBackground)
                        .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Ganancia Neta",
                                color = primaryGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = format.format(netProfit),
                                color = primaryGreen,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(primaryGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = primaryGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transacciones",
                        color = whiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "4 ventas",
                        color = Color(0x38, 0xBD, 0xF8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val transactionItems = listOf(
                        TransactionData("Pay de queso", "10:30 AM • Efectivo", "$45.00", Icons.Default.Payments),
                        TransactionData("Hot Cakes", "11:15 AM • Transferencia", "$35.00", Icons.Default.Smartphone),
                        TransactionData("Pay de queso", "12:45 PM • Efectivo", "$45.00", Icons.Default.Payments),
                        TransactionData("Hot Cakes", "01:20 PM • Transferencia", "$35.00", Icons.Default.Smartphone)
                    )

                    transactionItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(cardBackground)
                                .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(primaryGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = primaryGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = item.title,
                                    color = whiteText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.subtitle,
                                    color = secondaryText.copy(alpha = 0.6f),
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = item.price,
                                color = primaryGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onViewReceipts,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        contentColor = Color(0x0F, 0x17, 0x2A)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0x0F, 0x17, 0x2A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ver comprobantes",
                            color = Color(0x0F, 0x17, 0x2A),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(84.dp)
                .background(bottomNavBg)
                .border(
                    width = 1.dp,
                    color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailBottomNavItem(label = "Dashboard", icon = Icons.Default.GridView, isSelected = false, onClick = onDashboardClick)
                DetailBottomNavItem(label = "Productos", icon = Icons.Default.Inventory2, isSelected = false, onClick = onProductosClick)
                DetailBottomNavItem(label = "Historial", icon = Icons.Default.History, isSelected = true, onClick = onHistorialClick)
                DetailBottomNavItem(label = "Perfil", icon = Icons.Default.Person, isSelected = false, onClick = onPerfilClick)
            }
        }
    }
}

@Composable
fun DetailBottomNavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    Box(
        modifier = modifier.clip(RoundedCornerShape(20.dp)).clickable(onClick = onClick).padding(vertical = 6.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Row(
                modifier = Modifier.clip(RoundedCornerShape(50)).background(primaryGreen.copy(alpha = 0.15f)).padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = primaryGreen, modifier = Modifier.size(20.dp))
                Text(text = label, color = primaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = icon, contentDescription = label, tint = secondaryText.copy(alpha = 0.5f), modifier = Modifier.size(22.dp))
                Text(text = label, color = secondaryText.copy(alpha = 0.5f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

data class TransactionData(val title: String, val subtitle: String, val price: String, val icon: ImageVector)