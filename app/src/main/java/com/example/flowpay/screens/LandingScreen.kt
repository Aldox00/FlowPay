package com.example.flowpay.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.R
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Styling values
    val backgroundColor = Color(0x0F, 0x17, 0x2A) // #0F172A
    val primaryGreen = Color(0x22, 0xC5, 0x5E) // #22C55E
    val whiteText = Color(0xFF, 0xFF, 0xFF) // #FFFFFF
    val secondaryText = Color(0xD1, 0xD5, 0xDB) // #D1D5DB
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    // Gyroscope / Accelerometer Sensor logic for parallax effect
    val context = LocalContext.current
    var tiltX by remember { mutableFloatStateOf(0f) }
    var tiltY by remember { mutableFloatStateOf(0f) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        var isRotationVector = true

        if (sensor == null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            isRotationVector = false
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    if (isRotationVector && event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                        // event.values[0] is X * sin(theta/2), event.values[1] is Y * sin(theta/2)
                        // Scale rotation values for natural, subtle motion
                        tiltX = event.values[0] * 80f
                        tiltY = event.values[1] * 80f
                    } else if (!isRotationVector && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        // Accelerometer fallback (normalized around gravity g ~ 9.81 m/s^2)
                        tiltX = (-event.values[0] / 9.81f) * 80f
                        tiltY = (event.values[1] / 9.81f) * 80f
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (sensor != null) {
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val animatedTiltX by animateFloatAsState(
        targetValue = tiltX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tiltX"
    )
    val animatedTiltY by animateFloatAsState(
        targetValue = tiltY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tiltY"
    )

    val density = LocalDensity.current
    val tiltXPx = remember(animatedTiltX) { with(density) { animatedTiltX.dp.toPx() } }
    val tiltYPx = remember(animatedTiltY) { with(density) { animatedTiltY.dp.toPx() } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Decorative background glowing orbs with responsive multi-layered gyroscope parallax depth
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("parallax_background_orbs")
        ) {
            val width = size.width
            val height = size.height

            // Orb 1: Top-Right (Primary Green) - Shifts with intermediate depth
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryGreen.copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = width * 0.8f + (tiltXPx * 1.2f),
                        y = height * 0.2f + (tiltYPx * 1.2f)
                    ),
                    radius = width * 0.45f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x1E, 0x3A, 0x8A).copy(alpha = 0.22f),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = width * 0.1f - (tiltXPx * 0.8f),
                        y = height * 0.7f - (tiltYPx * 0.8f)
                    ),
                    radius = width * 0.5f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryGreen.copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(
                        x = width * 0.9f + (tiltXPx * 0.5f),
                        y = height * 0.5f + (tiltYPx * 0.5f)
                    ),
                    radius = width * 0.35f
                )
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Header Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo and Brand Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // AQUÍ YA ESTÁ TU LOGO REAL
                        Image(
                            painter = painterResource(id = R.drawable.logo_flowpay),
                            contentDescription = "Logo de FlowPay",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Text(
                            text = "FlowPay",
                            color = whiteText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    }

                }
            }

            // Hero Section with Badge, Main Title, and CTA buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Optimized for students badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(primaryGreen.copy(alpha = 0.15f))
                            .border(1.dp, primaryGreen.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = primaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Optimizado para Estudiantes",
                            color = primaryGreen,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    // Headline Title
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = whiteText)) {
                                append("Tu negocio universitario,\n")
                            }
                            withStyle(style = SpanStyle(color = primaryGreen)) {
                                append("bajo control.")
                            }
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Secondary Description
                    Text(
                        text = "Registra ventas, inversiones y ganancias en un solo lugar. La herramienta definitiva para que el emprendedor universitario tome decisiones basadas en datos reales.",
                        color = secondaryText,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    // CTA Buttons (Comenzar / Saber más)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Button(
                            onClick = onNavigateToRegister,
                            colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("comenzar_button"),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "Comenzar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        // Glassmorphism Saber más Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f))
                                .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = LocalIndication.current,
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(2)
                                        }
                                    }
                                )
                                .testTag("saber_mas_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Saber más",
                                color = whiteText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Social Proof (+500 students already use it)
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Avatar Pile (rendered dynamically)
                        Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                            listOf(
                                Color(0xFF, 0x3B, 0x82),
                                Color(0x3B, 0x82, 0xF6),
                                Color(0xF5, 0x9E, 0x0B)
                            ).forEachIndexed { index, color ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(2.dp, backgroundColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${(index + 65).toChar()}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = "+10 estudiantes ya lo usan",
                            color = secondaryText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Divider and Section "Diseñado para tu ritmo"
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Diseñado para tu ritmo",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = whiteText,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Simplifica tu contabilidad sin complicaciones técnicas.",
                        fontSize = 15.sp,
                        color = secondaryText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Glassmorphism Card 1: Análisis de Ganancias
            item {
                GlassCard(
                    title = "Análisis de Ganancias",
                    description = "Visualiza tus márgenes de beneficio por producto o servicio automáticamente. Toma el control total de tus finanzas.",
                    icon = Icons.Default.Info,
                    iconTint = primaryGreen
                )
            }

            // Glassmorphism Card 2: Recibos Rápidos
            item {
                GlassCard(
                    title = "Recibos Rápidos",
                    description = "Genera comprobantes digitales al instante para tus clientes.",
                    icon = Icons.AutoMirrored.Filled.List,
                    iconTint = primaryGreen,
                    actionText = "Pruébalo ahora ->",
                    onActionClick = { /* Quick receipts action */ }
                )
            }

            // Glassmorphism Card 3: Control de Stock
            item {
                GlassCard(
                    title = "Control de Stock",
                    description = "Nunca te quedes sin inventario en plena jornada universitaria.",
                    icon = Icons.Default.ShoppingCart,
                    iconTint = Color(0xEF, 0x44, 0x44) // Soft Red
                )
            }

            // Glassmorphism Card 4: Historial de Ventas with inline transaction rows
            item {
                GlassCard(
                    title = "Historial de Ventas",
                    description = "Un registro inmutable de cada transacción para que sepas exactamente de dónde viene cada centavo.",
                    icon = Icons.Default.Star,
                    iconTint = primaryGreen
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TransactionRow(
                            label = "Venta: Brownie",
                            amount = "+$2.50",
                            primaryGreen = primaryGreen,
                            iconColor = primaryGreen
                        )
                        TransactionRow(
                            label = "Venta: Café frío",
                            amount = "+$3.00",
                            primaryGreen = primaryGreen,
                            iconColor = primaryGreen
                        )
                    }
                }
            }

            // Bottom CTA Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.04f),
                                    Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.01f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.15f),
                                    Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.02f)
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Lleva tu emprendimiento al siguiente nivel",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = whiteText,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Únete a los cientos de estudiantes que ya están profesionalizando sus ventas en el campus con FlowPay.",
                            fontSize = 14.sp,
                            color = secondaryText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onNavigateToRegister,
                            colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("empieza_gratis_button")
                        ) {
                            Text(
                                text = "Empieza Gratis",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        }
                    }
                }
            }
        }
    }

@Composable
fun GlassCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconTint: Color,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    extraContent: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.03f)) // transparent white
            .border(
                width = 1.dp,
                color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f), // subtle white border
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xD1, 0xD5, 0xDB), // #D1D5DB
                lineHeight = 20.sp
            )

            // Extra Content inside card if provided (e.g. transaction list)
            if (extraContent != null) {
                extraContent()
            }

            // Action Link
            if (actionText != null && onActionClick != null) {
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable(onClick = onActionClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = actionText,
                        color = iconTint,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionRow(
    label: String,
    amount: String,
    primaryGreen: Color,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.04f))
            .border(1.dp, Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.06f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = label,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = amount,
            color = primaryGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
