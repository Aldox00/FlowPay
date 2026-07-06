package com.example.flowpay

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.Response
import com.example.flowpay.screens.JornadaRequest
import java.util.concurrent.TimeUnit

// 1. URL base apuntando a tu backend en la nube (Render)
object Constants {
    const val BASE_URL = "https://flowpay-backend-4xwj.onrender.com/api/"
}

// 2. El cliente de Retrofit listo para usar con el parche de tiempo
object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: FlowPayApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlowPayApiService::class.java)
    }
}

// 3. Los endpoints del backend mapeados correctamente
interface FlowPayApiService {
    @POST("auth/login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/registrar")
    suspend fun registrarUsuario(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/google")
    suspend fun loginConGoogle(@Body request: GoogleLoginRequest): Response<LoginResponse>

    // 🎯 CORREGIDO: Ruta exacta de tu Node.js para mandar el correo con Nodemailer
    @POST("auth/forgot-password")
    suspend fun solicitarRecuperacion(@Body request: SolicitarRecuperacionRequest): Response<GenericResponse>

    // 🎯 CORREGIDO: Ruta exacta de tu Node.js para cambiarla en la base de datos
    @POST("auth/reset-password")
    suspend fun restablecerContrasena(@Body request: RestablecerPasswordRequest): Response<GenericResponse>

    @POST("producto/crear")
    suspend fun crearProducto(@Body request: ProductRequest): Response<GenericProductResponse>

    // 🏪 ENDPOINT DE JORNADAS
    @POST("jornada/abrir")
    suspend fun abrirJornada(@Body request: JornadaRequest): Response<GenericJornadaResponse>

    // 🔒 @PUT: Para encajar perfectamente con tu ruta de Node.js sin romper nada
    @PUT("jornada/cerrar")
    suspend fun cerrarJornada(@Body request: CerrarJornadaRequest): Response<GenericJornadaResponse>

    // 🛒 ENDPOINT DE VENTAS
    @POST("venta/registrar")
    suspend fun registrarVenta(@Body request: VentaRequest): Response<GenericVentaResponse>

    // 📝 ENDPOINT AGREGADO: Para registrar la encuesta en el backend
    @POST("encuesta/registrar")
    suspend fun registrarEncuesta(@Body request: EncuestaRequest): Response<GenericEncuestaResponse>
}

// 4. Modelos de datos locales necesarios para que compile al 100%
data class LoginRequest(val correo: String, val contrasena: String)
data class GoogleLoginRequest(val idToken: String)

// 🟢 MODELOS PARA LA RECUPERACIÓN DE CONTRASEÑA
data class SolicitarRecuperacionRequest(val correo: String)
data class RestablecerPasswordRequest(val token: String, val nuevaContrasena: String)

// 🟢 RESPUESTA GENÉRICA PARA PETICIONES QUE SÓLO DEVUELVEN OK Y MSG
data class GenericResponse(
    val ok: Boolean,
    val msg: String,
    val token: String? = null // Captura el token que responde tu back en el JSON
)

data class LoginResponse(
    val ok: Boolean,
    val token: String?,
    val msg: String?,
    val usuario: UsuarioResponse?
)

data class UsuarioResponse(
    val id: Int,
    val nombre: String,
    val correo: String
)

data class RegisterRequest(val nombre: String, val correo: String, val contrasena: String)
data class AuthResponse(val login: Boolean?, val usuario_id: Int?, val token: String?)

data class ProductRequest(
    val usuario_id: Int,
    val nombre: String,
    @SerializedName("precio")
    val precio_venta: Double
)

data class GenericProductResponse(
    val ok: Boolean,
    val msg: String?,
    val producto_id: Int?
)

data class GenericJornadaResponse(
    val ok: Boolean,
    val msg: String?,
    val jornada_id: Int?
)

data class EncuestaRequest(
    val jornada_id: Int,
    val puntuacion_app: Int,
    val comentarios: String?
)

data class GenericEncuestaResponse(
    val ok: Boolean,
    val msg: String?
)

data class VentaRequest(
    val jornada_id: Int,
    val total: Double,
    val tipo_pago: String,
    @SerializedName("detalles")
    val detalles: List<DetalleVentaRequest>
)

data class DetalleVentaRequest(
    val producto_id: Int,
    val cantidad: Int,
    @SerializedName("precio_unitario")
    val precio_unitario: Double
)

data class GenericVentaResponse(
    val ok: Boolean,
    val msg: String?,
    val venta_id: Int?
)