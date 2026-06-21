package com.example.flowpay.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectPaymentScreen(
    productName: String,
    productPrice: String,
    onNavigateBack: () -> Unit,
    onNavigateToSurvey: () -> Unit,
    onNavigateToTransferProof: () -> Unit // <-- AGREGADO
) {
    val backgroundColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val greenColor = Color(0xFF1DB954)

    val productIcon = if (productName == "Hot Cakes") Icons.Default.BakeryDining else Icons.Default.Cake

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

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "VENTA RECIENTE", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = productName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "$$productPrice", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = " MXN", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 6.dp, start = 4.dp))
                    }
                }

                Surface(color = Color(0x1A1DB954), shape = RoundedCornerShape(14.dp), modifier = Modifier.size(52.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = productIcon, contentDescription = null, tint = greenColor, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(text = "¿Cómo te pagaron?", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = "Selecciona el método de recepción", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

        PaymentMethodRow(
            title = "Efectivo",
            subtitle = "Pago en mano",
            icon = Icons.Default.Payments,
            greenColor = greenColor,
            cardColor = cardColor,
            onClick = { onNavigateToSurvey() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PaymentMethodRow(
            title = "Transferencia",
            subtitle = "CoDi / SPEI • Podrás guardar el comprobante",
            icon = Icons.Default.QrCodeScanner,
            greenColor = greenColor,
            cardColor = cardColor,
            onClick = { onNavigateToTransferProof() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🛡️ Transacción segura • FlowPay ", fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun PaymentMethodRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    greenColor: Color,
    cardColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(width = 1.dp, color = Color(0x1AFFFFFF), shape = RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(color = Color(0x1A1DB954), shape = CircleShape, modifier = Modifier.size(48.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = null, tint = greenColor, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = subtitle, fontSize = 13.sp, color = Color.Gray)
                }
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.DarkGray)
        }
    }
}