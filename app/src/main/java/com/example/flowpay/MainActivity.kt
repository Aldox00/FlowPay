package com.example.flowpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flowpay.screens.LoginScreen
import com.example.flowpay.screens.RegisterScreen
import com.example.flowpay.screens.ForgotPasswordScreen
import com.example.flowpay.screens.DashboardScreen
import com.example.flowpay.ui.theme.FlowPayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowPayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    var registeredEmail by remember { mutableStateOf("") }
                    var registeredPassword by remember { mutableStateOf("") }

                    NavHost(navController = navController, startDestination = "login") {

                        // 1. PANTALLA: LOGIN
                        composable("login") {
                            LoginScreen(
                                registeredEmail = registeredEmail,
                                registeredPassword = registeredPassword,
                                onLoginSuccess = {
                                    // Al presionar Iniciar Sesión, va directo al Dashboard principal
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") },
                                onNavigateToForgotPassword = { navController.navigate("forgot_password") }
                            )
                        }

                        // 2. PANTALLA: REGISTRO (CREAR CUENTA)
                        composable("register") {
                            RegisterScreen(
                                onAccountCreated = { email, password ->
                                    // Guardamos las credenciales temporalmente
                                    registeredEmail = email
                                    registeredPassword = password

                                    // 👇 ¡CORREGIDO! Al presionar Registrarme, cerramos la vista y regresamos al LOGIN
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 3. PANTALLA: RECUPERAR CONTRASEÑA
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 4. PANTALLA: DASHBOARD PRINCIPAL
                        composable("dashboard") {
                            DashboardScreen()
                        }
                    }
                }
            }
        }
    }
}