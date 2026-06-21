package com.example.flowpay.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun TransferProofScreen(
    onNavigateBack: () -> Unit,
    onProofValidated: () -> Unit
) {
    val context = LocalContext.current

    val darkBgColor = Color(0xFF0F172A)
    val cardBgColor = Color(0xFF1E293B).copy(alpha = 0.6f)
    val greenColor = Color(0xFF1DB954)
    val lightGray = Color(0xFF94A3B8)
    val deleteRed = Color(0xFFEF4444)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            imageUri = tempCameraUri
        }
    }

    val requestGalleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val imagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        val partialGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissions[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] ?: false
        } else false

        if (imagesGranted || partialGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permiso de galería denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = File(context.cacheDir, "temp_proof_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF064E3B), darkBgColor),
                    endY = 500f
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        IconButton(
            onClick = { onNavigateBack() },
            modifier = Modifier
                .padding(top = 16.dp)
                .size(40.dp)
                .border(width = 1.dp, color = greenColor.copy(alpha = 0.4f), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Regresar",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Guarda el comprobante",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Toma foto o sube la captura de pantalla\ndel comprador.",
                    fontSize = 14.sp,
                    color = lightGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(
                            width = 1.dp,
                            color = greenColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .background(Color(0xFF0F172A).copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    val currentUri = imageUri
                    if (currentUri != null) {
                        val bitmap = remember(currentUri) {
                            if (android.os.Build.VERSION.SDK_INT < 28) {
                                @Suppress("DEPRECATION")
                                android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, currentUri)
                            } else {
                                val source = android.graphics.ImageDecoder.createSource(context.contentResolver, currentUri)
                                android.graphics.ImageDecoder.decodeBitmap(source)
                            }
                        }

                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Comprobante cargado",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar foto",
                                tint = deleteRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                color = greenColor.copy(alpha = 0.12f),
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = greenColor,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Alinea el comprobante\ndentro del marco",
                                fontSize = 14.sp,
                                color = greenColor.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        val length = 22.dp.toPx()
                        val stroke = 2.dp.toPx()
                        drawLine(color = greenColor, start = Offset(0f, 0f), end = Offset(length, 0f), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(0f, 0f), end = Offset(0f, length), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(size.width, 0f), end = Offset(size.width - length, 0f), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(size.width, 0f), end = Offset(size.width, length), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(0f, size.height), end = Offset(length, size.height), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(0f, size.height), end = Offset(0f, size.height - length), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(size.width, size.height), end = Offset(size.width - length, size.height), strokeWidth = stroke)
                        drawLine(color = greenColor, start = Offset(size.width, size.height), end = Offset(size.width, size.height - length), strokeWidth = stroke)
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = {
                        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            val file = File(context.cacheDir, "temp_proof_${System.currentTimeMillis()}.jpg")
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                            tempCameraUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Tomar foto", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            requestGalleryPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.READ_MEDIA_IMAGES,
                                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                                )
                            )
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestGalleryPermissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                        } else {
                            requestGalleryPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Subir desde galería", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (imageUri != null) {
            Button(
                onClick = { onProofValidated() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Validar y Continuar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}