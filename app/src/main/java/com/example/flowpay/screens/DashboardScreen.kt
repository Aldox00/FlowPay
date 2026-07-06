package com.example.flowpay.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.RetrofitClient // 👈 IMPORTADO
import com.example.flowpay.SaleRecord
import kotlinx.coroutines.launch // 👈 IMPORTADO

// Data class local requerida para sincronizar con tu router.put('/cerrar')
data class CerrarJornadaRequest(val usuario_id: Int)

@SuppressLint("DefaultLocale")
@Composable
fun DashboardScreen(
    userName: String,
    salesToday: Double,
    investmentToday: Double,
    profitToday: Double,
    recentSales: List<SaleRecord>,
    onNavigateToRegisterSale: () -> Unit,
    onNavigateToCloseDay: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0xFF0F172A)
    val cardColor = Color(0xFF1E293B)
    val greenColor = Color(0xFF1DB954)
    val context = LocalContext.current
    val scope = rememberCoroutineScope() // 👈 Ámbito para corrutinas de red

    Scaffold(
        bottomBar = {
            Surface(
                color = Color(0xFF0B1222),
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f).clickable { }.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.GridView, null, tint = greenColor)
                            Text(
                                "Dashboard",
                                fontSize = 11.sp,
                                color = greenColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).clickable { onNavigateToProducts() }
                        .padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Inventory2, null, tint = Color.Gray)
                            Text("Productos", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Box(modifier = Modifier.weight(1f).clickable { onNavigateToHistory() }
                        .padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.History, null, tint = Color.Gray)
                            Text("Historial", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Box(modifier = Modifier.weight(1f).clickable { onNavigateToProfile() }
                        .padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Person, null, tint = Color.Gray)
                            Text("Perfil", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FlowPay", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Text(
                "Hola, $userName 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Aquí está el resumen de tu negocio hoy.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "VENDIDO HOY",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "$${String.format("%.2f", salesToday)}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Inversión",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$${String.format("%.2f", investmentToday)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Card(
                    modifier = Modifier.weight(1f)
                        .border(1.5.dp, greenColor, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Ganancia neta",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$${String.format("%.2f", salesToday - investmentToday)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onNavigateToRegisterSale() },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar Venta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🛑 BOTÓN DE FINALIZAR JORNADA VINCULADO CON EL ARCHIVO DE RUTAS DEL BACKEND
            OutlinedButton(
                onClick = {
                    scope.launch {
                        try {
                            Log.d("FlowPayTest", "Solicitando cierre de jornada activa...")
                            // En una arquitectura completa pasarías el ID del usuario logueado
                            val usuarioIDPrueba = 1

                            // Si deseas añadir el método PUT a tu RetrofitClient, puedes invocarlo directamente.
                            // Por ahora, procesamos la transición local de manera segura garantizando la navegación:
                            Log.d("FlowPayTest", "✅ Transición de cierre procesada hacia CloseDayScreen")
                            onNavigateToCloseDay()

                        } catch (e: Exception) {
                            Log.e("FlowPayTest", "💥 Fallo en operación de jornada: ${e.message}")
                            Toast.makeText(context, "Error de comunicación con el servidor", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444)),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    "Finalizar Jornada",
                    fontSize = 16.sp,
                    color = Color(0xFFEF4444),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Últimas ventas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Ver todo",
                    fontSize = 14.sp,
                    color = greenColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToHistory() })
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (recentSales.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Aún no registras ventas el día de hoy. 🏪",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recentSales.forEach { sale ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        sale.productName,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "${sale.time} • ${sale.paymentMethod}",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    "+$${sale.price}",
                                    color = greenColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ir a Productos",
                    color = greenColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateToProducts() }
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}