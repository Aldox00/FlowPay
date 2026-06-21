package com.example.flowpay.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
fun RegisterScreen(onAccountCreated: (String, String) -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_flowpay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { onAccountCreated("", "") },
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Regresar",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color(0x4D112233),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0x33FFFFFF),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_flowpay),
                contentDescription = "Logo FlowPay",
                modifier = Modifier
                    .size(75.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Únete y controla tus ganancias diarias.",
                fontSize = 14.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(text = "Nombre Completo", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = fullName,
                onValueChange = { input ->
                    if (input.length <= 50 && input.all { it.isLetter() || it.isWhitespace() }) {
                        fullName = input
                    }
                },
                label = "Ej. Juan Pérez",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(text = "Correo Electrónico", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = email,
                onValueChange = { input ->
                    val isValidChar = input.all { it.isLetterOrDigit() || it == '@' || it == '.' || it == '_' || it == '-' }
                    if (input.length <= 40 && isValidChar) {
                        email = input
                    }
                },
                label = "correo@ejemplo.com",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(text = "Contraseña", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = password,
                onValueChange = { input ->
                    if (input.length <= 20 && input.all { it.isLetterOrDigit() }) {
                        password = input
                    }
                },
                label = "Mínimo 8 caracteres",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(text = "Confirmar Contraseña", color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp))
            FlowPayTextField(
                value = confirmPassword,
                onValueChange = { input ->
                    if (input.length <= 20 && input.all { it.isLetterOrDigit() }) {
                        confirmPassword = input
                    }
                },
                label = "Repite tu contraseña",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Button(
                onClick = {
                    if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        if (password.length >= 8) {
                            if (password == confirmPassword) {
                                Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                                onAccountCreated(email, password)
                            } else {
                                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "La contraseña debe tener mínimo 8 caracteres", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Registrarme", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "¿Ya tienes una cuenta? ", color = Color.LightGray, fontSize = 13.sp)
                Text(
                    text = "Iniciar sesión",
                    color = Color(0xFF1DB954),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onAccountCreated("", "") }
                )
            }
        }
    }
}