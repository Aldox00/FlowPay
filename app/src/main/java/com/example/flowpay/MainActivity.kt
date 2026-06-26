package com.example.flowpay

import android.annotation.SuppressLint
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
import com.example.flowpay.screens.*
import com.example.flowpay.ui.theme.FlowPayTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowPayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    var registeredName by remember { mutableStateOf("Estudiante") }
                    var registeredEmail by remember { mutableStateOf("") }
                    var registeredPassword by remember { mutableStateOf("") }

                    var totalSalesToday by remember { mutableDoubleStateOf(0.0) }
                    var totalInvestmentToday by remember { mutableDoubleStateOf(0.0) }
                    val totalProfitToday by derivedStateOf { totalSalesToday - totalInvestmentToday }

                    val historyRecords = remember {
                        mutableStateListOf(
                            DailyRecord("12 de Junio", 2850.0, 1100.0, 1750.0),
                            DailyRecord("11 de Junio", 1920.5, 850.0, 1070.5),
                            DailyRecord("10 de Junio", 3100.0, 1400.0, 1700.0)
                        )
                    }

                    NavHost(navController = navController, startDestination = "landing") {

                        composable("landing") {
                            LandingScreen(
                                onNavigateToLogin = { navController.navigate("login") },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onAccountCreated = { name, email, password ->
                                    if (name.isNotBlank()) registeredName = name
                                    registeredEmail = email
                                    registeredPassword = password

                                    navController.navigate("login") {
                                        popUpTo("landing")
                                    }
                                }
                            )
                        }

                        composable("login") {
                            LoginScreen(
                                registeredEmail = registeredEmail,
                                registeredPassword = registeredPassword,
                                onLoginSuccess = {
                                    navController.navigate("initial_setup") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") },
                                onNavigateToForgotPassword = { navController.navigate("forgot_password") }
                            )
                        }

                        composable("initial_setup") {
                            InitialSetupScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onContinue = { p1Name, p1Price, p2Name, p2Price ->
                                    navController.navigate("dashboard") {
                                        popUpTo("initial_setup") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("forgot_password") {
                            ForgotPasswordScreen(onNavigateBack = { navController.popBackStack() })
                        }

                        composable(route = "dashboard") {
                            DashboardScreen(
                                userName = registeredName,
                                salesToday = totalSalesToday,
                                investmentToday = totalInvestmentToday,
                                profitToday = totalProfitToday,
                                onNavigateToRegisterSale = {
                                    navController.navigate("register_sale")
                                }
                            )
                        }

                        composable(route = "register_sale") {
                            RegisterSaleScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToSelectPayment = { productName, productPrice ->
                                    navController.navigate("select_payment/$productName/$productPrice")
                                }
                            )
                        }

                        composable(route = "select_payment/{productName}/{productPrice}") { backStackEntry ->
                            val productName = backStackEntry.arguments?.getString("productName") ?: "Producto"
                            val productPrice = backStackEntry.arguments?.getString("productPrice") ?: "0.00"

                            SelectPaymentScreen(
                                productName = productName,
                                productPrice = productPrice,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToSurvey = {
                                    val price = productPrice.toDoubleOrNull() ?: 0.0
                                    val estimatedInvestment = if (productName == "Hot Cakes") 10.0 else 12.0

                                    totalSalesToday += price
                                    totalInvestmentToday += estimatedInvestment

                                    val todayStr = SimpleDateFormat("dd 'de' MMMM", Locale("es", "MX")).format(Date())
                                    val existingIndex = historyRecords.indexOfFirst { it.date == todayStr }

                                    if (existingIndex != -1) {
                                        val oldRecord = historyRecords[existingIndex]
                                        historyRecords[existingIndex] = DailyRecord(
                                            date = todayStr,
                                            sales = oldRecord.sales + price,
                                            investment = oldRecord.investment + estimatedInvestment,
                                            profit = (oldRecord.sales + price) - (oldRecord.investment + estimatedInvestment)
                                        )
                                    } else {
                                        historyRecords.add(0, DailyRecord(todayStr, price, estimatedInvestment, price - estimatedInvestment))
                                    }

                                    navController.navigate("survey")
                                },

                                onNavigateToTransferProof = {
                                    navController.navigate("transfer_proof/$productName/$productPrice")
                                }
                            )
                        }

                        composable(route = "transfer_proof/{productName}/{productPrice}") { backStackEntry ->
                            val productName = backStackEntry.arguments?.getString("productName") ?: "Producto"
                            val productPrice = backStackEntry.arguments?.getString("productPrice") ?: "0.00"

                            TransferProofScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onProofValidated = {

                                    val price = productPrice.toDoubleOrNull() ?: 0.0
                                    val estimatedInvestment = if (productName == "Hot Cakes") 10.0 else 12.0

                                    totalSalesToday += price
                                    totalInvestmentToday += estimatedInvestment

                                    val todayStr = SimpleDateFormat("dd 'de' MMMM", Locale("es", "MX")).format(Date())
                                    val existingIndex = historyRecords.indexOfFirst { it.date == todayStr }

                                    if (existingIndex != -1) {
                                        val oldRecord = historyRecords[existingIndex]
                                        historyRecords[existingIndex] = DailyRecord(
                                            date = todayStr,
                                            sales = oldRecord.sales + price,
                                            investment = oldRecord.investment + estimatedInvestment,
                                            profit = (oldRecord.sales + price) - (oldRecord.investment + estimatedInvestment)
                                        )
                                    } else {
                                        historyRecords.add(0, DailyRecord(todayStr, price, estimatedInvestment, price - estimatedInvestment))
                                    }


                                    navController.navigate("survey")
                                }
                            )
                        }

                        composable(route = "survey") {
                            SurveyScreen(
                                onSurveySubmitted = {
                                    navController.navigate("history") {
                                        popUpTo("dashboard") { inclusive = false }
                                    }
                                }
                            )
                        }

                        composable(route = "history") {
                            HistoryScreen(
                                records = historyRecords,
                                onNavigateToDashboard = {
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DailyRecord(
    val date: String,
    val sales: Double,
    val investment: Double,
    val profit: Double
)