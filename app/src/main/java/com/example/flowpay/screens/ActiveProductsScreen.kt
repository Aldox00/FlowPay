package com.example.flowpay.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActiveProductsScreen(
    onNavigateBack: () -> Unit,
    onContinue: (selectedProducts: List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Styling constants
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.6f)

    // List of static/configured products (max 2)
    val products = remember {
        listOf(
            ProductItem("Pay de queso", "$30.00 MXN", Icons.Default.Cake),
            ProductItem("Hot Cakes", "$25.00 MXN", Icons.Default.BakeryDining)
        )
    }

    // State for selected products
    val selectedProducts = remember { mutableStateListOf<String>() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Decorative background glowing orbs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top-left green glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryGreen.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(width * 0.1f, height * 0.2f),
                    radius = width * 0.5f
                )
            )

            // Right-center subtle teal glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x38, 0xBD, 0xF8).copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.9f, height * 0.6f),
                    radius = width * 0.6f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Top Bar with arrow back icon button
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
                        tint = primaryGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title and Subtitle
            Text(
                text = "¿Qué vas a vender hoy?",
                color = whiteText,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Selecciona los productos que ofrecerás en esta jornada.",
                color = secondaryText.copy(alpha = 0.7f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Product Cards List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                products.forEach { product ->
                    val isChecked = selectedProducts.contains(product.name)

                    // Card border dynamic state
                    val borderWithColor by animateColorAsState(
                        targetValue = if (isChecked) primaryGreen else Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f),
                        label = "borderColor"
                    )
                    val borderWidthDp by animateDpAsState(
                        targetValue = if (isChecked) 2.dp else 1.dp,
                        label = "borderWidth"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(
                                width = borderWidthDp,
                                color = borderWithColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                if (isChecked) {
                                    selectedProducts.remove(product.name)
                                } else {
                                    selectedProducts.add(product.name)
                                }
                            }
                            .padding(20.dp)
                            .testTag("product_active_card_${product.name}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Custom styled checkbox
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedProducts.add(product.name)
                                } else {
                                    selectedProducts.remove(product.name)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = primaryGreen,
                                uncheckedColor = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.3f),
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Product custom styled icon container
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isChecked) primaryGreen.copy(alpha = 0.15f)
                                    else Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.05f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = product.icon,
                                contentDescription = null,
                                tint = if (isChecked) primaryGreen else secondaryText,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Product information
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = product.name,
                                color = whiteText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = product.price,
                                color = primaryGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Main CTA Button
            val isButtonEnabled = selectedProducts.isNotEmpty()
            Button(
                onClick = {
                    if (isButtonEnabled) {
                        onContinue(selectedProducts.toList())
                    }
                },
                enabled = isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryGreen,
                    disabledContainerColor = primaryGreen.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("continuar_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Continuar",
                        color = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (isButtonEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

data class ProductItem(
    val name: String,
    val price: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)