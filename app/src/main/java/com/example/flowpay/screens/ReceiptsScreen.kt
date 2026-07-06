package com.example.flowpay.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // 🎯 NUEVA IMPORTACIÓN: Carga imágenes desde URIs de forma eficiente
import com.example.flowpay.SaleRecord
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReceiptsScreen(
    transferSales: List<SaleRecord>,
    onNavigateBack: () -> Unit,
    onReceiptClick: (SaleRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)

    val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera de la pantalla
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = whiteText)
            }
            Column {
                Text("Comprobantes", color = whiteText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Galería de transferencias", color = secondaryText, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LÓGICA DE CONTROL: Si no hay transferencias, muestra la vista vacía. Si hay, muestra la galería.
        if (transferSales.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ImageNotSupported,
                contentDescription = null,
                tint = secondaryText.copy(alpha = 0.3f),
                modifier = Modifier.size(80.dp).padding(bottom = 16.dp)
            )
            Text(
                text = "Sin comprobantes guardados",
                color = whiteText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Las imágenes de transferencias se vincularán automáticamente cuando conectes tu base de datos.",
                color = secondaryText,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1.5f))
        } else {
            // Grid de 2 columnas con los comprobantes simulados/existentes de la venta
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(transferSales) { sale ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.85f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF, 0xFF, 0xFF).copy(alpha = 0.08f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onReceiptClick(sale) }
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 🎯 CONTENEDOR MODIFICADO: Renderiza la imagen real si existe el URI
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x0F, 0x17, 0x2A)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!sale.imageUri.isNullOrBlank()) {
                                    AsyncImage(
                                        model = sale.imageUri,
                                        contentDescription = "Comprobante de ${sale.productName}",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop // Recorta la imagen para que rellene el cuadro estéticamente
                                    )
                                } else {
                                    // Respaldo por si se guardó sin foto
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Comprobante sin imagen",
                                        tint = primaryGreen.copy(alpha = 0.6f),
                                        modifier = Modifier.size(44.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Información de la transferencia vinculada
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = sale.productName,
                                    color = whiteText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = format.format(sale.price),
                                        color = primaryGreen,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = sale.time,
                                        color = secondaryText.copy(alpha = 0.6f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}