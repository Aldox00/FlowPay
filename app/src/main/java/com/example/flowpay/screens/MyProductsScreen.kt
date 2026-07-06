package com.example.flowpay.screens

import android.net.Uri
import android.util.Log // 👈 IMPORTADO PARA MONITOREAR CON LOGCAT
import android.widget.Toast // 👈 IMPORTADO PARA AVISARTE SI CONECTÓ
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalContext // 👈 IMPORTADO
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flowpay.Product
import com.example.flowpay.ProductRequest // 👈 IMPORTADO DESDE TU RETROFITCLIENT
import com.example.flowpay.RetrofitClient // 👈 IMPORTADO DESDE TU RETROFITCLIENT
import kotlinx.coroutines.launch // 👈 IMPORTADO PARA CORRUTINAS ASÍNCRONAS

@Composable
fun MyProductsScreen(
    products: List<Product>,
    onNavigateBack: () -> Unit,
    onAddProductClick: (String, Double, String?) -> Unit,
    onEditProductClick: (Int, String, Double, String?) -> Unit,
    onDeleteProductClick: (Int) -> Unit,
    onDashboardClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onPerfilClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current // 👈 Obtenemos contexto para los Toasts
    val scope = rememberCoroutineScope() // 👈 Lanzador de peticiones HTTP en segundo plano

    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)
    val bottomNavBg = Color(0x0B, 0x0F, 0x19)
    val deleteRed = Color(0xEF, 0x44, 0x44)

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var tempName by remember { mutableStateOf("") }
    var tempPrice by remember { mutableStateOf("") }
    var editingProductId by remember { mutableStateOf<Int?>(null) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Box(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            drawCircle(brush = Brush.radialGradient(listOf(primaryGreen.copy(alpha = 0.12f), Color.Transparent), center = Offset(width * 0.2f, height * 0.3f), radius = width * 0.6f))
        }

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(bottom = 84.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = whiteText)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Mis Productos", color = whiteText, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Gestiona el catálogo de tu negocio.", color = secondaryText.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(products) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBackground)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

                            if (product.imageUri != null) {
                                AsyncImage(
                                    model = product.imageUri,
                                    contentDescription = product.name,
                                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0x0F, 0x17, 0x2A).copy(alpha = 0.6f)).border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                    Icon(imageVector = product.icon, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(22.dp))
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.name, color = whiteText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$${product.price}", color = primaryGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        tempName = product.name
                                        tempPrice = product.price.toString()
                                        selectedImageUri = product.imageUri?.let { Uri.parse(it) }
                                        editingProductId = product.id
                                        showEditDialog = true
                                    },
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(primaryGreen.copy(alpha = 0.1f))
                                ) {
                                    Icon(Icons.Default.Edit, "Editar", tint = primaryGreen, modifier = Modifier.size(18.dp))
                                }
                                IconButton(
                                    onClick = { onDeleteProductClick(product.id) },
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(deleteRed.copy(alpha = 0.1f))
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = deleteRed, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                tempName = ""
                tempPrice = ""
                selectedImageUri = null
                showAddDialog = true
            },
            containerColor = primaryGreen,
            contentColor = Color(0xFF0F172A),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(bottom = 100.dp, end = 24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar producto", modifier = Modifier.size(28.dp))
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
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyProductsBottomNavItem(label = "Dashboard", icon = Icons.Default.GridView, isSelected = false, onClick = onDashboardClick, modifier = Modifier.weight(1f))
                MyProductsBottomNavItem(label = "Productos", icon = Icons.Default.Inventory2, isSelected = false, onClick = { }, modifier = Modifier.weight(1f))
                MyProductsBottomNavItem(label = "Historial", icon = Icons.Default.History, isSelected = false, onClick = onHistorialClick, modifier = Modifier.weight(1f))
                MyProductsBottomNavItem(label = "Perfil", icon = Icons.Default.Person, isSelected = true, onClick = onPerfilClick, modifier = Modifier.weight(1f))
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = cardBackground,
                title = { Text("Nuevo Producto", color = whiteText, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A).copy(alpha = 0.4f))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Foto del producto",
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(32.dp))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Toca para agregar foto", color = secondaryText, fontSize = 13.sp)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Nombre del producto", color = secondaryText) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedBorderColor = primaryGreen),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = tempPrice,
                            onValueChange = { tempPrice = it },
                            label = { Text("Precio", color = secondaryText) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedBorderColor = primaryGreen),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = tempPrice.toDoubleOrNull() ?: 0.0
                            if (tempName.isNotBlank() && price > 0) {

                                // 🚀 CONEXIÓN CON TU BACKEND NODE.JS
                                scope.launch {
                                    try {
                                        Log.d("FlowPayTest", "Guardando producto en la API: $tempName")

                                        // 🔥 CAMBIO CLAVE: Se usó "precio_venta" en lugar de "precio"
                                        // para acoplarse al cambio que hicimos en el RetrofitClient.
                                        val productReq = ProductRequest(
                                            usuario_id = 5,
                                            nombre = tempName.trim(),
                                            precio_venta = price
                                        )

                                        val respuesta = RetrofitClient.apiService.crearProducto(productReq)

                                        if (respuesta.isSuccessful) {
                                            Log.d("FlowPayTest", "✅ Producto registrado con éxito en MySQL")
                                            Toast.makeText(context, "¡Producto guardado exitosamente!", Toast.LENGTH_SHORT).show()

                                            onAddProductClick(tempName, price, selectedImageUri?.toString())
                                            showAddDialog = false
                                        } else {
                                            val errorMsg = respuesta.errorBody()?.string()
                                            Log.e("FlowPayTest", "❌ Error de validación del servidor: $errorMsg")
                                            Toast.makeText(context, "El servidor rechazó el producto", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Log.e("FlowPayTest", "💥 Fallo crítico de red: ${e.message}")
                                        Toast.makeText(context, "Fallo de conexión local con el servidor", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                    ) { Text("Guardar", color = whiteText) }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) { Text("Cancelar", color = secondaryText) }
                }
            )
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                containerColor = cardBackground,
                title = { Text("Editar Producto", color = whiteText, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A).copy(alpha = 0.4f))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Foto del producto",
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(32.dp))
                                    Spacer(Modifier.height(8.dp))
                                    Text("Toca para agregar foto", color = secondaryText, fontSize = 13.sp)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Nombre del producto", color = secondaryText) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedBorderColor = primaryGreen),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = tempPrice,
                            onValueChange = { tempPrice = it },
                            label = { Text("Precio", color = secondaryText) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedBorderColor = primaryGreen),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = tempPrice.toDoubleOrNull() ?: 0.0
                            if (tempName.isNotBlank() && price > 0 && editingProductId != null) {
                                onEditProductClick(editingProductId!!, tempName, price, selectedImageUri?.toString())
                                showEditDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                    ) { Text("Actualizar", color = whiteText) }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("Cancelar", color = secondaryText) }
                }
            )
        }
    }
}

@Composable
private fun MyProductsBottomNavItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
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