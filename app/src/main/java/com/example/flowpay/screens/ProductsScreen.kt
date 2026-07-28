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
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flowpay.Product

@Composable
fun ProductsScreen(
    products: List<Product>,
    onNavigateToSelectPayment: (productId: Int, productName: String, productPrice: String) -> Unit,
    onNavigateToMyProducts: () -> Unit,
    onDashboardClick: () -> Unit,
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

    Box(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            drawCircle(brush = Brush.radialGradient(listOf(primaryGreen.copy(alpha = 0.12f), Color.Transparent), center = Offset(width * 0.5f, height * 0.2f), radius = width * 0.5f))
        }

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(bottom = 84.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Venta Rápida", color = whiteText, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Toca un producto para registrar la venta", color = secondaryText.copy(alpha = 0.8f), fontSize = 15.sp)
                }

                IconButton(
                    onClick = onNavigateToMyProducts,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(primaryGreen.copy(alpha = 0.12f))
                        .border(1.dp, primaryGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Administrar productos",
                        tint = primaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .clickable {
                                onNavigateToSelectPayment(product.id, product.name, product.price.toString())
                            },
                        colors = CardDefaults.cardColors(containerColor = cardBackground)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if (product.imageUri != null) {
                                AsyncImage(
                                    model = product.imageUri,
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0x0F, 0x17, 0x2A).copy(alpha = 0.6f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = product.icon, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(22.dp))
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.name, color = whiteText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$${product.price}", color = primaryGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VentaBottomNavItem(label = "Dashboard", icon = Icons.Default.GridView, isSelected = false, onClick = onDashboardClick, modifier = Modifier.weight(1f))
                VentaBottomNavItem(label = "Productos", icon = Icons.Default.Inventory2, isSelected = true, onClick = { }, modifier = Modifier.weight(1f))
                VentaBottomNavItem(label = "Historial", icon = Icons.Default.History, isSelected = false, onClick = onHistorialClick, modifier = Modifier.weight(1f))
                VentaBottomNavItem(label = "Perfil", icon = Icons.Default.Person, isSelected = false, onClick = onPerfilClick, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun VentaBottomNavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
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