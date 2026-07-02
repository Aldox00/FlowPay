package com.example.flowpay.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flowpay.Product

@Composable
fun RegisterSaleScreen(
    products: List<Product>,
    onNavigateBack: () -> Unit,
    onNavigateToSelectPayment: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = primaryGreen)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(primaryGreen.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("VENTA RÁPIDA", color = primaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Registrar Venta", color = whiteText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Toca un producto para registrar la venta.", color = Color.Gray, fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                        .clickable {
                            onNavigateToSelectPayment(product.name, product.price.toString())
                        },
                    colors = CardDefaults.cardColors(containerColor = cardBackground)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            if (product.imageUri != null) {
                                AsyncImage(
                                    model = product.imageUri,
                                    contentDescription = product.name,
                                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)),
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

                            Column {
                                Text(text = product.name, color = whiteText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$${product.price}", color = primaryGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = { onNavigateToSelectPayment(product.name, product.price.toString()) },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Vender", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}