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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.flowpay.screens.*
import com.example.flowpay.ui.theme.FlowPayTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

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
    val time: String,
    val imageUri: String? = null
)

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val icon: androidx.compose.ui.graphics.vector.ImageVector = androidx.compose.material.icons.Icons.Default.Cake,
    val imageUri: String? = null
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)

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

                    // 🎯 CONTROL CENTRALIZADO MULTIUSUARIO DE ALUMNOS
                    var usuarioIdSesion by remember { mutableIntStateOf(0) }
                    var jornadaIdSesion by remember { mutableIntStateOf(0) }

                    val productCatalog = remember {
                        mutableStateListOf<Product>()
                    }

                    var totalSalesToday by remember { mutableDoubleStateOf(0.0) }
                    var totalInvestmentToday by remember { mutableDoubleStateOf(0.0) }
                    val totalProfitToday by derivedStateOf { totalSalesToday - totalInvestmentToday }
                    val todaySales = remember { mutableStateListOf<SaleRecord>() }

                    val historyRecords = remember { mutableStateListOf<DailyRecord>() }
                    var hasSurveyedThisWeek by remember { mutableStateOf(false) }
                    var hasSurveyedThisMonth by remember { mutableStateOf(false) }
                    var pendingFilterAfterSurvey by remember { mutableStateOf("Semana") }

                    fun syncTodayHistory() {
                        val todayStr = SimpleDateFormat("dd 'de' MMMM", Locale("es", "MX")).format(Date())
                        val existingIndex = historyRecords.indexOfFirst { it.date == todayStr }
                        val currentProfit = totalSalesToday - totalInvestmentToday

                        if (existingIndex != -1) {
                            historyRecords[existingIndex] = DailyRecord(
                                date = todayStr,
                                sales = totalSalesToday,
                                investment = totalInvestmentToday,
                                profit = currentProfit
                            )
                        } else {
                            historyRecords.add(0, DailyRecord(
                                date = todayStr,
                                sales = totalSalesToday,
                                investment = totalInvestmentToday,
                                profit = currentProfit
                            ))
                        }
                    }

                    // Acepta el parámetro opcional de la URI de la foto
                    fun registerSale(productName: String, price: Double, method: String, imageUri: String? = null) {
                        totalSalesToday += price
                        val timeStr = SimpleDateFormat("hh:mm a", Locale("es", "MX")).format(Date())
                        todaySales.add(SaleRecord(productName, price, method, timeStr, imageUri))
                        syncTodayHistory()
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
                                // 🟢 ACTUALIZADO: Ahora recibe dinámicamente nombre y correo desde el login (manual o Google)
                                onLoginSuccess = { idUsuarioRecibido, nombreUsuarioRecibido, correoUsuarioRecibido ->
                                    usuarioIdSesion = idUsuarioRecibido

                                    // 🎯 Seteamos los datos reales entregados por tu base de datos en la nube
                                    if (!nombreUsuarioRecibido.isNullOrBlank()) registeredName = nombreUsuarioRecibido
                                    if (!correoUsuarioRecibido.isNullOrBlank()) registeredEmail = correoUsuarioRecibido

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
                            ForgotPasswordScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToRestablecer = { tokenRecibido ->
                                    navController.navigate("restablecer_password/$tokenRecibido")
                                }
                            )
                        }

                        composable("restablecer_password/{token}") { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token") ?: ""
                            RestablecerPasswordScreen(
                                tokenRecuperacion = token,
                                onPasswordChangedSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
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
                                usuarioIdActivo = usuarioIdSesion,
                                onDismiss = { navController.popBackStack() },
                                onSaveAndStart = { investmentAmount, idJornadaObtenido ->
                                    totalInvestmentToday += investmentAmount
                                    jornadaIdSesion = idJornadaObtenido
                                    syncTodayHistory()
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
                                jornadaIdActiva = jornadaIdSesion,
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
                                jornadaId = jornadaIdSesion,
                                onNavigateBack = { navController.popBackStack() },
                                onProofValidated = { uriReal ->
                                    val price = productPrice.toDoubleOrNull() ?: 0.0
                                    registerSale(productName, price, "Transferencia", uriReal)
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
                            val scope = rememberCoroutineScope()
                            val context = androidx.compose.ui.platform.LocalContext.current

                            CloseDayScreen(
                                jornadaId = jornadaIdSesion,
                                totalSales = totalSalesToday,
                                totalInvestment = totalInvestmentToday,
                                netProfit = totalProfitToday,
                                onNavigateBack = { navController.popBackStack() },
                                onFinalizeDay = {
                                    scope.launch {
                                        try {
                                            android.util.Log.d("FlowPayTest", "Guardando encuesta automática para Jornada ID: $jornadaIdSesion...")

                                            val respuestaEncuesta = RetrofitClient.apiService.registrarEncuesta(
                                                EncuestaRequest(
                                                    jornada_id = jornadaIdSesion,
                                                    puntuacion_app = 5,
                                                    comentarios = "Jornada cerrada exitosamente"
                                                )
                                            )

                                            if (respuestaEncuesta.isSuccessful && respuestaEncuesta.body()?.ok == true) {
                                                android.util.Log.d("FlowPayTest", "✅ Encuesta registrada correctamente.")
                                            } else {
                                                android.util.Log.e("FlowPayTest", "⚠️ No se guardó la encuesta o fue omitida por el backend.")
                                            }

                                            android.util.Log.d("FlowPayTest", "Solicitando cierre en la API para Jornada ID: $jornadaIdSesion...")

                                            val efectivoTotal = todaySales.filter { it.paymentMethod == "Efectivo" }.sumOf { it.price }
                                            val transferenciaTotal = todaySales.filter { it.paymentMethod == "Transferencia" }.sumOf { it.price }
                                            val encContestada = if (hasSurveyedThisWeek || hasSurveyedThisMonth) 1 else 0

                                            val respuesta = RetrofitClient.apiService.cerrarJornada(
                                                CerrarJornadaRequest(
                                                    jornada_id = jornadaIdSesion,
                                                    monto_inversion = totalInvestmentToday,
                                                    monto_ventas_efectivo = efectivoTotal,
                                                    monto_ventas_transferencia = transferenciaTotal,
                                                    ganancia_neta = totalProfitToday,
                                                    encuesta_contestada = encContestada
                                                )
                                            )

                                            if (respuesta.isSuccessful) {
                                                android.util.Log.d("FlowPayTest", "✅ Jornada finalizada con éxito en MySQL.")
                                                android.widget.Toast.makeText(context, "¡Día guardado con éxito!", android.widget.Toast.LENGTH_SHORT).show()
                                            } else {
                                                android.util.Log.e("FlowPayTest", "❌ Error del servidor en el cierre: ${respuesta.errorBody()?.string()}")
                                                android.widget.Toast.makeText(context, "El servidor rechazó el cierre de caja", android.widget.Toast.LENGTH_LONG).show()
                                            }
                                        } catch (e: Exception) {
                                            android.util.Log.e("FlowPayTest", "💥 Fallo crítico de red al cerrar jornada: ${e.message}")
                                            android.widget.Toast.makeText(context, "Error de conexión al guardar el día", android.widget.Toast.LENGTH_SHORT).show()
                                        } finally {
                                            totalSalesToday = 0.0
                                            totalInvestmentToday = 0.0
                                            jornadaIdSesion = 0
                                            usuarioIdSesion = 0
                                            todaySales.clear()

                                            navController.navigate("landing") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable("history") {
                            HistoryScreen(
                                records = historyRecords,
                                onNavigateToDashboard = {
                                    navController.navigate("dashboard") { popUpTo(0) }
                                },
                                onNavigateToProductos = {
                                    navController.navigate("products") { popUpTo(0) }
                                },
                                onNavigateToPerfil = {
                                    navController.navigate("profile") { popUpTo(0) }
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
                            val sales = backStackEntry.arguments?.getString("sales") ?: "0.0"
                            val investment = backStackEntry.arguments?.getString("investment") ?: "0.0"
                            val profit = backStackEntry.arguments?.getString("profit")?.toDoubleOrNull() ?: 0.0

                            DayDetailScreen(
                                date = date,
                                totalSalesStr = sales,
                                totalInvestmentStr = investment,
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
                            val transferenciasDelDia = todaySales.filter { it.paymentMethod == "Transferencia" }

                            ReceiptsScreen(
                                transferSales = transferenciasDelDia,
                                onNavigateBack = { navController.popBackStack() },
                                onReceiptClick = { sale ->
                                    android.util.Log.d("FlowPayTest", "Click en el comprobante de: ${sale.productName}")
                                }
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
                                    jornadaIdSesion = 0
                                    usuarioIdSesion = 0
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
                                onAddProductClick = { name, price, imageUri ->
                                    val newId = (productCatalog.maxOfOrNull { it.id } ?: 0) + 1
                                    productCatalog.add(Product(newId, name, price, imageUri = imageUri))
                                },
                                onEditProductClick = { productId, newName, newPrice, imageUri ->
                                    val index = productCatalog.indexOfFirst { it.id == productId }
                                    if (index != -1) {
                                        val oldProduct = productCatalog[index]
                                        productCatalog[index] = Product(
                                            id = productId,
                                            name = newName,
                                            price = newPrice,
                                            imageUri = imageUri ?: oldProduct.imageUri
                                        )
                                    }
                                },
                                onDeleteProductClick = { productId ->
                                    productCatalog.removeAll { it.id == productId }
                                },
                                onDashboardClick = { navController.navigate("dashboard") { popUpTo(0) } },
                                onHistorialClick = { navController.navigate("history") { popUpTo(0) } }, // 🟢 CORREGIDO: Estructura limpia sin caracteres rotos
                                onPerfilClick = { navController.navigate("profile") { popUpTo(0) } }
                            )
                        }

                        composable("privacy") {
                            PrivacyScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    } // Cierre correcto de NavHost
                } // Cierre correcto de Surface
            } // Cierre correcto de FlowPayTheme
        } // Cierre correcto de setContent
    } // Cierre correcto de onCreate
} // Cierre correcto de MainActivity