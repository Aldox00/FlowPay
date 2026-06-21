package com.example.flowpay.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowpay.R
import com.example.flowpay.components.FlowPayTextField

@Composable
fun ForgotPasswordScreen(onNavigateBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 👇 SOLUCIÓN AQUÍ: Cambiado a R.drawable.login para usar tu fondo real y evitar el crash
        // 👇 Cambia 'login' por 'bg_flowpay' para solucionar el error
        Image(
            painter = painterResource(id = R.drawable.bg_flowpay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Barra Superior con Botón Regresar e Identificador de App
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Regresar",
                    tint = Color(0xFF1DB954) // Verde Figma
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "FlowPay",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1DB954),
                modifier = Modifier.padding(end = 40.dp) // Compensa el botón para centrarlo
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Tarjeta flotante centrada estilo Cristal Oscuro
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
                .background(
                    color = Color(0xED111A2E), // Tono oscuro profundo de tu captura
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0x1AFFFFFF),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono Circular Verde de Reinicio/Candado
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .background(Color(0x1A1DB954), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔄", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recuperar\ncontraseña",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Te enviaremos un enlace para restablecer tu acceso",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Campo de Email
            Text(
                text = "CORREO ELECTRÓNICO",
                color = Color.LightGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
            )
            FlowPayTextField(
                value = email,
                onValueChange = { input ->
                    val isValidChar = input.all { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' || it == '-' }
                    if (input.length <= 30 && isValidChar) email = input
                },
                label = "nombre@ejemplo.com",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón Verde con la Flecha
            Button(
                onClick = {
                    if (email.isNotBlank() && email.contains("@") && email.contains(".")) {
                        Toast.makeText(context, "Enlace enviado a tu correo", Toast.LENGTH_LONG).show()
                        onNavigateBack()
                    } else {
                        Toast.makeText(context, "Por favor introduce un correo válido", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Enviar enlace de recuperación ", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "➔", fontSize = 15.sp, color = Color.White)
                }
            }
        }

        // Enlace inferior para regresar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "¿Recordaste tu contraseña? ", color = Color.Gray, fontSize = 13.sp)
            Text(
                text = "Iniciar sesión",
                color = Color(0xFF1DB954),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onNavigateBack() }
            )
        }
    }
}