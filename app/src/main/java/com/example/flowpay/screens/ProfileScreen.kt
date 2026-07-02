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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    onNotificationToggle: (Boolean) -> Unit,
    onReminderTimeClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onMisProductosClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onProductosClick: () -> Unit,
    onHistorialClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPrivacyClick: () -> Unit
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)
    val bottomNavBg = Color(0x0B, 0x0F, 0x19)

    var notificationsEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val initials = userName.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")

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
                    center = Offset(width * 0.3f, height * 0.2f),
                    radius = width * 0.5f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x38, 0xBD, 0xF8).copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.8f, height * 0.7f),
                    radius = width * 0.6f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(bottom = 84.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FlowPay",
                    color = primaryGreen,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("app_title")
                )

                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = "Notificaciones",
                    tint = primaryGreen,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(primaryGreen)
                            .testTag("profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = whiteText,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Text(
                        text = userName,
                        color = whiteText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = userEmail,
                        color = secondaryText.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBackground)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = secondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Notificaciones",
                                color = whiteText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = {
                                notificationsEnabled = it
                                onNotificationToggle(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = whiteText,
                                checkedTrackColor = primaryGreen,
                                uncheckedThumbColor = secondaryText,
                                uncheckedTrackColor = Color.DarkGray
                            ),
                            modifier = Modifier.testTag("notifications_switch")
                        )
                    }

                    HorizontalDivider(color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onReminderTimeClick)
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = secondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Hora del recordatorio",
                                color = whiteText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "2:00 PM",
                                color = primaryGreen,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = secondaryText.copy(alpha = 0.5f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onEditProfileClick)
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = secondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Editar perfil",
                                color = whiteText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = secondaryText.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    HorizontalDivider(color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onMisProductosClick)
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = secondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Mis Productos",
                                color = whiteText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = secondaryText.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBackground)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable(onClick = onLogoutClick)
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                        .testTag("logout_button_row")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Salir",
                            tint = Color(0xEF, 0x44, 0x44),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Cerrar sesión",
                            color = Color(0xEF, 0x44, 0x44),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
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
                    isSelected = false,
                    onClick = onHistorialClick,
                    modifier = Modifier.testTag("nav_historial")
                )

                BottomNavItem(
                    label = "Perfil",
                    icon = Icons.Default.Person,
                    isSelected = true,
                    onClick = { },
                    modifier = Modifier.testTag("nav_perfil")
                )
            }
        }
    }
}