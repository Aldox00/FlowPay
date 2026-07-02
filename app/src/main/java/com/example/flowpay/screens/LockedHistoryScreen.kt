package com.example.flowpay.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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

@Composable
fun LockedHistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSurvey: () -> Unit,
    onDashboardClick: () -> Unit,
    onProductosClick: () -> Unit,
    onPerfilClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.6f)
    val bottomNavBg = Color(0x0B, 0x0F, 0x19)

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
                    center = Offset(width * 0.8f, height * 0.3f),
                    radius = width * 0.6f
                )
            )


            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x38, 0xBD, 0xF8).copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.2f, height * 0.7f),
                    radius = width * 0.6f
                )
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 80.dp)
                .blur(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Historial",
                color = whiteText,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            repeat(5) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBackground)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (index % 2 == 0) "Venta - Pay de queso" else "Venta - Hot Cakes",
                            color = whiteText.copy(alpha = 0.4f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hace ${index + 1} horas",
                            color = secondaryText.copy(alpha = 0.3f),
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = if (index % 2 == 0) "+$150.00" else "+$100.00",
                        color = primaryGreen.copy(alpha = 0.4f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x0F, 0x17, 0x2A).copy(alpha = 0.3f))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(28.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .testTag("blocked_history_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.85f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(primaryGreen.copy(alpha = 0.15f))
                            .border(1.5.dp, primaryGreen, CircleShape)
                            .testTag("lock_icon_badge"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Bloqueado",
                            tint = primaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Historial Bloqueado",
                        color = whiteText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Completa la encuesta de tu última jornada para ver tu historial",
                        color = secondaryText.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onNavigateToSurvey,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            contentColor = Color(0x0F, 0x17, 0x2A)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("to_survey_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ir a la encuesta",
                                color = Color(0x0F, 0x17, 0x2A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0x0F, 0x17, 0x2A),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
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
                BottomNavItem(
                    label = "Dashboard",
                    icon = Icons.Default.GridView,
                    isSelected = false,
                    onClick = onDashboardClick,
                    modifier = Modifier.testTag("nav_dashboard")
                )

                BottomNavItem(
                    label = "Productos",
                    icon = Icons.Default.Inventory2,
                    isSelected = false,
                    onClick = onProductosClick,
                    modifier = Modifier.testTag("nav_productos")
                )

                BottomNavItem(
                    label = "Historial",
                    icon = Icons.Default.History,
                    isSelected = true,
                    onClick = { /* Already here */ },
                    modifier = Modifier.testTag("nav_historial")
                )

                BottomNavItem(
                    label = "Perfil",
                    icon = Icons.Default.Person,
                    isSelected = false,
                    onClick = onPerfilClick,
                    modifier = Modifier.testTag("nav_perfil")
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(primaryGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = primaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = label,
                    color = primaryGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = secondaryText.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = label,
                    color = secondaryText.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}