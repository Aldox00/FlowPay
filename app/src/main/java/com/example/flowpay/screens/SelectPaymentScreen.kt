package com.example.flowpay.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.RetrofitClient
import com.example.flowpay.VentaRequest
import com.example.flowpay.DetalleVentaRequest
import kotlinx.coroutines.launch

@Composable
fun SelectPaymentScreen(
    jornadaIdActiva: Int,
    productName: String,
    productPrice: String,
    onNavigateBack: () -> Unit,
    onCashSelected: () -> Unit,
    onNavigateToTransferProof: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val backgroundColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val greenColor = Color(0xFF1DB954)

    val productIcon = if (productName == "Hot Cakes") Icons.Default.BakeryDining else Icons.Default.Cake

    Column(
        modifier = modifier
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
            onClick = {
                if (jornadaIdActiva <= 0) {
                    Toast.makeText(context, "Error: No hay una jornada activa válida. Reinicia sesión.", Toast.LENGTH_LONG).show()
                    Log.e("FlowPayTest", "🛑 Abortando venta: jornadaIdActiva es $jornadaIdActiva")
                    return@PaymentMethodRow
                }

                val precioNumero = productPrice.toDoubleOrNull() ?: 0.0

                scope.launch {
                    try {
                        Log.d("FlowPayTest", "Enviando venta en Efectivo al Backend con Jornada ID: $jornadaIdActiva...")

                        val idProductoDinamico = if (productName == "Hot Cakes") 1 else 2

                        val detalleUnico = DetalleVentaRequest(
                            producto_id = idProductoDinamico,
                            cantidad = 1,
                            precio_unitario = precioNumero
                        )

                        val ventaReq = VentaRequest(
                            jornada_id = jornadaIdActiva,
                            total = precioNumero,
                            tipo_pago = "Efectivo",
                            detalles = listOf(detalleUnico)
                        )

                        val respuesta = RetrofitClient.apiService.registrarVenta(ventaReq)

                        if (respuesta.isSuccessful) {
                            Log.d("FlowPayTest", "✅ ¡Venta y detalles insertados con éxito en MySQL!")
                            Toast.makeText(context, "¡Venta guardada en la Base de Datos!", Toast.LENGTH_SHORT).show()
                            onCashSelected()
                        } else {
                            val errorBody = respuesta.errorBody()?.string() ?: ""
                            Log.e("FlowPayTest", "❌ El servidor rechazó la transacción: $errorBody")
                            Toast.makeText(context, "El servidor rechazó la venta.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.e("FlowPayTest", "💥 Fallo de red al intentar registrar venta: ${e.message}")
                        Toast.makeText(context, "Error de conexión con Node.js", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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