package com.example.flowpay.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.Product

@SuppressLint("DefaultLocale")
@Composable
fun ActiveProductsScreen(
    products: List<Product>,
    onNavigateBack: () -> Unit,
    onContinue: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.6f)

    val selectedProducts = remember { mutableStateListOf<String>() }

    Box(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(brush = Brush.radialGradient(listOf(primaryGreen.copy(alpha = 0.1f), Color.Transparent), center = Offset(size.width * 0.8f, size.height * 0.2f), radius = size.width * 0.5f))
        }

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp)) {
            // Header
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = whiteText)
            }

            Text("¿Qué vas a vender hoy?", color = whiteText, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

            // Lista de productos
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                products.forEach { product ->
                    val isChecked = selectedProducts.contains(product.name)
                    val borderColor by animateColorAsState(if (isChecked) primaryGreen else Color.Transparent)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable {
                                if (isChecked) selectedProducts.remove(product.name)
                                else selectedProducts.add(product.name)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isChecked, onCheckedChange = null)
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(product.name, color = whiteText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("$${String.format("%.2f", product.price)}", color = primaryGreen)                        }
                    }
                }
            }

            Button(
                onClick = { onContinue(selectedProducts.toList()) },
                enabled = selectedProducts.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
            ) {
                Text("Continuar", fontWeight = FontWeight.Bold)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}