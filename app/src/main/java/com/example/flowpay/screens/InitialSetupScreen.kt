package com.example.flowpay.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    cornerRadius: Dp = 0.dp,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 4.dp
) = drawWithContent {
    drawContent()
    val stroke = Stroke(
        width = width.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
            phase = 0f
        )
    )
    if (cornerRadius > 0.dp) {
        drawRoundRect(
            color = color,
            style = stroke,
            cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
        )
    } else {
        drawRect(
            color = color,
            style = stroke
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialSetupScreen(
    onNavigateBack: () -> Unit,
    onContinue: (p1Name: String, p1Price: String, p2Name: String, p2Price: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.7f)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val neonTeal = Color(0x38, 0xBD, 0xF8)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val grayLight = Color(0xD1, 0xD5, 0xDB)
    val grayIntermediate = Color(0x9C, 0xA3, 0xAF)
    val grayMuted = Color(0x4B, 0x55, 0x63)

    var product1Name by remember { mutableStateOf("") }
    var product1Price by remember { mutableStateOf("") }
    var product2Name by remember { mutableStateOf("") }
    var product2Price by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top center green ambient orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryGreen.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.1f),
                    radius = width * 0.5f
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(neonTeal.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.9f),
                    radius = width * 0.6f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
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
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Solo se muestra una vez al crear tu cuenta.",
                    color = grayIntermediate,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Configura tus productos",
                    color = whiteText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Registra los productos que vendes.",
                    color = grayIntermediate,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            ProductCard(
                label = "PRODUCTO 1",
                labelColor = primaryGreen,
                nameValue = product1Name,
                onNameChange = { product1Name = it },
                priceValue = product1Price,
                onPriceChange = { product1Price = it },
                cardBgColor = cardBackground,
                whiteText = whiteText,
                grayIntermediate = grayIntermediate,
                grayMuted = grayMuted,
                placeholderName = "Ej. Pay de queso",
                modifier = Modifier.testTag("product_card_1")
            )

            ProductCard(
                label = "PRODUCTO 2 (OPCIONAL)",
                labelColor = grayIntermediate,
                nameValue = product2Name,
                onNameChange = { product2Name = it },
                priceValue = product2Price,
                onPriceChange = { product2Price = it },
                cardBgColor = cardBackground,
                whiteText = whiteText,
                grayIntermediate = grayIntermediate,
                grayMuted = grayMuted,
                placeholderName = "Ej. Café frío",
                modifier = Modifier.testTag("product_card_2")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (product1Name.isBlank() || product1Price.isBlank()) {
                        Toast.makeText(context, "Por favor configura el Producto 1", Toast.LENGTH_SHORT).show()
                    } else {
                        onContinue(product1Name, product1Price, product2Name, product2Price)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_continue_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Guardar y continuar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}

@Composable
fun ProductCard(
    label: String,
    labelColor: Color,
    nameValue: String,
    onNameChange: (String) -> Unit,
    priceValue: String,
    onPriceChange: (String) -> Unit,
    cardBgColor: Color,
    whiteText: Color,
    grayIntermediate: Color,
    grayMuted: Color,
    placeholderName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .border(
                width = 1.dp,
                color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Card Product Identifier
            Text(
                text = label,
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )

            // Name Field Block
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "NOMBRE DEL PRODUCTO",
                    color = grayIntermediate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                TextField(
                    value = nameValue,
                    onValueChange = onNameChange,
                    placeholder = {
                        Text(
                            text = placeholderName,
                            color = grayMuted,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x0F, 0x17, 0x2A).copy(alpha = 0.4f),
                        unfocusedContainerColor = Color(0x0F, 0x17, 0x2A).copy(alpha = 0.4f),
                        disabledContainerColor = Color(0x0F, 0x17, 0x2A).copy(alpha = 0.4f),
                        focusedTextColor = whiteText,
                        unfocusedTextColor = whiteText,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("input_name_$label")
                )
            }

            // Price Field Block
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PRECIO",
                    color = grayIntermediate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                TextField(
                    value = priceValue,
                    onValueChange = onPriceChange,
                    placeholder = {
                        Text(
                            text = "$0.00",
                            color = grayMuted,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = "Dinero",
                            tint = grayIntermediate,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        Text(
                            text = "MXN",
                            color = grayIntermediate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x0F, 0x17, 0x2A).copy(alpha = 0.4f),
                        unfocusedContainerColor = Color(0x0F, 0x17, 0x2A).copy(alpha = 0.4f),
                        disabledContainerColor = Color(0x0F, 0x17, 0x2A).copy(alpha = 0.4f),
                        focusedTextColor = whiteText,
                        unfocusedTextColor = whiteText,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("input_price_$label")
                )
            }
        }
    }
}