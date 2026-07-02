package com.example.flowpay.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditProfileScreen(
    currentName: String,
    currentEmail: String,
    onNavigateBack: () -> Unit,
    onSaveProfile: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0x0F, 0x17, 0x2A)
    val primaryGreen = Color(0x22, 0xC5, 0x5E)
    val whiteText = Color(0xFF, 0xFF, 0xFF)
    val secondaryText = Color(0xD1, 0xD5, 0xDB)
    val cardBackground = Color(0x1E, 0x29, 0x3B).copy(alpha = 0.65f)

    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var newPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(listOf(primaryGreen.copy(alpha = 0.15f), Color.Transparent), center = Offset(size.width * 0.5f, 0f), radius = size.width * 0.7f)
            )
        }

        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            Row(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.offset(x = (-12).dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = whiteText)
                }
                Text("Editar Perfil", color = whiteText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardBackground)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre completo", color = secondaryText) },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = primaryGreen) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedIndicatorColor = primaryGreen),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo universitario", color = secondaryText) },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = primaryGreen) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedIndicatorColor = primaryGreen),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva Contraseña (Opcional)", color = secondaryText) },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = primaryGreen) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = whiteText, unfocusedTextColor = whiteText, focusedIndicatorColor = primaryGreen),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        onSaveProfile(name, email, newPassword)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen, contentColor = Color(0x0F, 0x17, 0x2A)),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}