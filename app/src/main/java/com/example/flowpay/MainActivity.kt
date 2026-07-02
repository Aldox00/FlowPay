package com.example.flowpay

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.flowpay.screens.*
import com.example.flowpay.ui.theme.FlowPayTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DailyRecord(
    val date: String,
    val sales: Double,
    val investment: Double,
    val profit: Double
)

data class SaleRecord(
    val productName: String,
    val price: Double,
    val paymentMethod: String,
    val time: String
)

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val icon: androidx.compose.ui.graphics.vector.ImageVector = androidx.compose.material.icons.Icons.Default.Cake
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                    var hasCompletedSetup by remember { mutableStateOf(false) }

                    val productCatalog = remember {
                        mutableStateListOf<Product>()
                    }

                    var totalSalesToday by remember { mutableDoubleStateOf(0.0) }
                    var totalInvestmentToday by remember { mutableDoubleStateOf(0.0) }
                    val totalProfitToday by derivedStateOf { totalSalesToday - totalInvestmentToday }
                    val todaySales = remember { mutableStateListOf<SaleRecord>() }

                    val historyRecords = remember {
                        mutableStateListOf(
                            DailyRecord("12 de Junio", 2850.0, 1100.0, 1750.0),
                            DailyRecord("11 de Junio", 1920.5, 850.0, 1070.5),
                            DailyRecord("10 de Junio", 3100.0, 1400.0, 1700.0)
                        )
                    }

                    var hasSurveyedThisWeek by remember { mutableStateOf(false) }
                    var hasSurveyedThisMonth by remember { mutableStateOf(false) }
                    var pendingFilterAfterSurvey by remember { mutableStateOf("Semana") }

                    fun registerSale(productName: String, price: Double, method: String) {
                        totalSalesToday += price
                        val timeStr = SimpleDateFormat("hh:mm a", Locale("es", "MX")).format(Date())
                        todaySales.add(SaleRecord(productName, price, method, timeStr))

                        val todayStr = SimpleDateFormat("dd 'de' MMMM", Locale("es", "MX")).format(Date())
                        val existingIndex = historyRecords.indexOfFirst { it.date == todayStr }
                        if (existingIndex != -1) {
                            val old = historyRecords[existingIndex]
                            historyRecords[existingIndex] = DailyRecord(
                                date = todayStr,
                                sales = old.sales + price,
                                investment = old.investment,
                                profit = (old.sales + price) - old.investment
                            )
                        } else {
                            historyRecords.add(0, DailyRecord(todayStr, price, 0.0, price))
                        }
                    }
                    var tempRegName by remember { mutableStateOf("") }
                    var tempRegEmail by remember { mutableStateOf("") }
                    var tempRegPassword by remember { mutableStateOf("") }
                    var tempRegConfirmPassword by remember { mutableStateOf("") }
                    var tempRegPrivacyAccepted by remember { mutableStateOf(false) }

                    NavHost(navController = navController, startDestination = "landing") {



                        composable("landing") {
                            LandingScreen(
                                onNavigateToLogin = { navController.navigate("login") },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }

                        composable(route = "register") {
                            RegisterScreen(
                                initialName = tempRegName,
                                initialEmail = tempRegEmail,
                                initialPassword = tempRegPassword,
                                initialConfirmPassword = tempRegConfirmPassword,
                                initialPrivacyAccepted = tempRegPrivacyAccepted,
                                onNameChange = { tempRegName = it },
                                onEmailChange = { tempRegEmail = it },
                                onPasswordChange = { tempRegPassword = it },
                                onConfirmPasswordChange = { tempRegConfirmPassword = it },
                                onPrivacyAcceptedChange = { tempRegPrivacyAccepted = it },
                                onAccountCreated = { name, email, password ->
                                    if (name.isNotBlank()) registeredName = name
                                    registeredEmail = email
                                    registeredPassword = password
                                    tempRegName = ""
                                    tempRegEmail = ""
                                    tempRegPassword = ""
                                    tempRegConfirmPassword = ""
                                    tempRegPrivacyAccepted = false

                                    navController.navigate(route = "login") { popUpTo(route = "landing") }
                                },
                                onPrivacyClick = { navController.navigate(route = "privacy") }
                            )
                        }

                        composable("login") {
                            LoginScreen(
                                registeredEmail = registeredEmail,
                                registeredPassword = registeredPassword,
                                onLoginSuccess = {
                                    if (hasCompletedSetup) {
                                        navController.navigate("active_products") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("initial_setup") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") },
                                onNavigateToForgotPassword = { navController.navigate("forgot_password") }
                            )
                        }

                        composable("forgot_password") {
                            ForgotPasswordScreen(onNavigateBack = { navController.popBackStack() })
                        }

                        composable("initial_setup") {
                            InitialSetupScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onContinue = { p1Name, p1Price, p2Name, p2Price ->
                                    productCatalog.clear()
                                    productCatalog.add(Product(1, p1Name, p1Price.toDoubleOrNull() ?: 0.0))
                                    if (p2Name.isNotBlank()) {
                                        productCatalog.add(Product(2, p2Name, p2Price.toDoubleOrNull() ?: 0.0))
                                    }
                                    hasCompletedSetup = true
                                    navController.navigate("active_products") {
                                        popUpTo("initial_setup") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("active_products") {
                            ActiveProductsScreen(
                                products = productCatalog,
                                onNavigateBack = { navController.navigate("login") { popUpTo(0) } },
                                onContinue = { selectedProducts ->
                                    navController.navigate("investment_modal")
                                }
                            )
                        }

                        dialog("investment_modal") {
                            InvestmentModalScreen(
                                onDismiss = { navController.popBackStack() },
                                onSaveAndStart = { investmentAmount ->
                                    totalInvestmentToday += investmentAmount
                                    navController.navigate("dashboard") {
                                        popUpTo("active_products") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("dashboard") {
                            DashboardScreen(
                                userName = registeredName,
                                salesToday = totalSalesToday,
                                investmentToday = totalInvestmentToday,
                                profitToday = totalProfitToday,
                                recentSales = todaySales.takeLast(3).reversed(),
                                onNavigateToRegisterSale = { navController.navigate("register_sale") },
                                onNavigateToCloseDay = { navController.navigate("close_day") },
                                onNavigateToProducts = { navController.navigate("products") { popUpTo(0) } },
                                onNavigateToHistory = { navController.navigate("history") { popUpTo(0) } },
                                onNavigateToProfile = { navController.navigate("profile") { popUpTo(0) } }
                            )
                        }

                        composable("register_sale") {
                            RegisterSaleScreen(
                                products = productCatalog,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToSelectPayment = { productName, productPrice ->
                                    navController.navigate("select_payment/$productName/$productPrice")
                                }
                            )
                        }

                        composable("select_payment/{productName}/{productPrice}") { backStackEntry ->
                            val productName = backStackEntry.arguments?.getString("productName") ?: "Producto"
                            val productPrice = backStackEntry.arguments?.getString("productPrice") ?: "0.00"

                            SelectPaymentScreen(
                                productName = productName,
                                productPrice = productPrice,
                                onNavigateBack = { navController.popBackStack() },
                                onCashSelected = {
                                    val price = productPrice.toDoubleOrNull() ?: 0.0
                                    registerSale(productName, price, "Efectivo")
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                },
                                onNavigateToTransferProof = {
                                    navController.navigate("transfer_proof/$productName/$productPrice")
                                }
                            )
                        }

                        composable("transfer_proof/{productName}/{productPrice}") { backStackEntry ->
                            val productName = backStackEntry.arguments?.getString("productName") ?: "Producto"
                            val productPrice = backStackEntry.arguments?.getString("productPrice") ?: "0.00"

                            TransferProofScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onProofValidated = {
                                    val price = productPrice.toDoubleOrNull() ?: 0.0
                                    registerSale(productName, price, "Transferencia")
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("products") {
                            ProductsScreen(
                                products = productCatalog,
                                onNavigateToSelectPayment = { productName, productPrice ->
                                    navController.navigate("select_payment/$productName/$productPrice")
                                },
                                onDashboardClick = { navController.navigate("dashboard") { popUpTo(0) } },
                                onHistorialClick = { navController.navigate("history") { popUpTo(0) } },
                                onPerfilClick = { navController.navigate("profile") { popUpTo(0) } }
                            )
                        }



                        composable("close_day") {
                            CloseDayScreen(
                                totalSales = totalSalesToday,
                                totalInvestment = totalInvestmentToday,
                                netProfit = totalProfitToday,
                                onNavigateBack = { navController.popBackStack() },
                                onFinalizeDay = {
                                    totalSalesToday = 0.0
                                    totalInvestmentToday = 0.0
                                    todaySales.clear()
                                    navController.navigate("dashboard") { popUpTo(0) }
                                }
                            )
                        }


                        composable("history") {
                            HistoryScreen(
                                records = historyRecords,
                                onNavigateToDashboard = {
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                },
                                onNavigateToSurvey = { filter ->
                                    pendingFilterAfterSurvey = filter
                                    val alreadySurveyed = when (filter) {
                                        "Semana" -> hasSurveyedThisWeek
                                        "Mes" -> hasSurveyedThisMonth
                                        else -> true
                                    }
                                    if (!alreadySurveyed) {
                                        navController.navigate("survey")
                                    }
                                },
                                onNavigateToDayDetail = { date, sales, investment, profit ->
                                    navController.navigate("day_detail/$date/$sales/$investment/$profit")
                                }
                            )
                        }

                        composable("survey") {
                            SurveyScreen(
                                onSurveySubmitted = {
                                    when (pendingFilterAfterSurvey) {
                                        "Semana" -> hasSurveyedThisWeek = true
                                        "Mes" -> hasSurveyedThisMonth = true
                                    }
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("day_detail/{date}/{sales}/{investment}/{profit}") { backStackEntry ->
                            val date = backStackEntry.arguments?.getString("date") ?: "Sin fecha"
                            val sales = backStackEntry.arguments?.getString("sales")?.toDoubleOrNull() ?: 0.0
                            val investment = backStackEntry.arguments?.getString("investment")?.toDoubleOrNull() ?: 0.0
                            val profit = backStackEntry.arguments?.getString("profit")?.toDoubleOrNull() ?: 0.0

                            DayDetailScreen(
                                date = date,
                                totalSales = sales,
                                totalInvestment = investment,
                                netProfit = profit,
                                onNavigateBack = { navController.popBackStack() },
                                onViewReceipts = { navController.navigate("receipts") },
                                onDashboardClick = { navController.navigate("dashboard") { popUpTo(0) } },
                                onProductosClick = { navController.navigate("products") { popUpTo(0) } },
                                onHistorialClick = { navController.navigate("history") { popUpTo(0) } },
                                onPerfilClick = { navController.navigate("profile") { popUpTo(0) } }
                            )
                        }

                        composable("receipts") {
                            ReceiptsScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onReceiptClick = { /* TODO: abrir imagen en grande */ }
                            )
                        }


                        composable("locked_history") {
                            LockedHistoryScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToSurvey = { navController.navigate("survey") },
                                onDashboardClick = { navController.navigate("dashboard") { popUpTo(0) } },
                                onProductosClick = { navController.navigate("products") { popUpTo(0) } },
                                onPerfilClick = { navController.navigate("profile") { popUpTo(0) } }
                            )
                        }


                        composable("profile") {
                            ProfileScreen(
                                userName = registeredName,
                                userEmail = registeredEmail,
                                onNotificationToggle = { },
                                onReminderTimeClick = { },
                                onEditProfileClick = { navController.navigate("edit_profile") },
                                onMisProductosClick = { navController.navigate("my_products") },
                                onPrivacyClick = { navController.navigate("privacy") },
                                onLogoutClick = {
                                    totalSalesToday = 0.0
                                    totalInvestmentToday = 0.0
                                    todaySales.clear()
                                    navController.navigate("login") { popUpTo(0) }
                                },
                                onDashboardClick = { navController.navigate("dashboard") { popUpTo(0) } },
                                onProductosClick = { navController.navigate("products") { popUpTo(0) } },
                                onHistorialClick = { navController.navigate("history") { popUpTo(0) } }
                            )
                        }

                        composable("edit_profile") {
                            EditProfileScreen(
                                currentName = registeredName,
                                currentEmail = registeredEmail,
                                onNavigateBack = { navController.popBackStack() },
                                onSaveProfile = { name, email, newPassword ->
                                    if (name.isNotBlank()) registeredName = name
                                    if (email.isNotBlank()) registeredEmail = email
                                    if (newPassword.isNotBlank()) registeredPassword = newPassword
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("my_products") {
                            MyProductsScreen(
                                products = productCatalog,
                                onNavigateBack = { navController.popBackStack() },
                                onAddProductClick = { name, price ->
                                    if (productCatalog.size < 2) {
                                        val newId = (productCatalog.maxOfOrNull { it.id } ?: 0) + 1
                                        productCatalog.add(Product(newId, name, price))
                                    }
                                },
                                onEditProductClick = { productId, newName, newPrice ->
                                    val index = productCatalog.indexOfFirst { it.id == productId }
                                    if (index != -1) {
                                        productCatalog[index] = Product(productId, newName, newPrice)
                                    }
                                },
                                onDeleteProductClick = { productId ->
                                    productCatalog.removeAll { it.id == productId }
                                },
                                onDashboardClick = { navController.navigate("dashboard") { popUpTo(0) } },
                                onHistorialClick = { navController.navigate("history") { popUpTo(0) } },
                                onPerfilClick = { navController.navigate("profile") { popUpTo(0) } }
                            )
                        }

                        composable("privacy") {
                            PrivacyScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}