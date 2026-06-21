package com.example.flowpay.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterSaleScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSelectPayment: (String, String) -> Unit
) {
    val backgroundColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val greenColor = Color(0xFF1DB954)
    val badgeBgColor = Color(0x1A1DB954)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        IconButton(
            onClick = { onNavigateBack() },
            modifier = Modifier.padding(top = 16.dp).size(32.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Regresar", tint = greenColor)
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(color = badgeBgColor, shape = RoundedCornerShape(20.dp), modifier = Modifier.padding(bottom = 24.dp)) {
                Text(text = "VENTA 1", color = greenColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
            }

            Text(text = "Registrar Venta", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
            Text(text = "Toca un producto para registrar la venta.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp, bottom = 32.dp))

            ProductSaleCard(
                title = "Pay de queso",
                price = "30.00",
                icon = Icons.Default.Cake,
                cardColor = cardColor,
                greenColor = greenColor,
                onVenderClick = { onNavigateToSelectPayment("Pay de queso", "30.00") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductSaleCard(
                title = "Hot Cakes",
                price = "25.00",
                icon = Icons.Default.BakeryDining,
                cardColor = cardColor,
                greenColor = greenColor,
                onVenderClick = { onNavigateToSelectPayment("Hot Cakes", "25.00") }
            )
        }
    }
}

@Composable
fun ProductSaleCard(
    title: String,
    price: String,
    icon: ImageVector,
    cardColor: Color,
    greenColor: Color,
    onVenderClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(color = Color(0x1AFFFFFF), shape = CircleShape, modifier = Modifier.size(52.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = null, tint = greenColor, modifier = Modifier.size(28.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "$$price", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = greenColor)
                }
            }

            Button(
                onClick = { onVenderClick() },
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Vender", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}