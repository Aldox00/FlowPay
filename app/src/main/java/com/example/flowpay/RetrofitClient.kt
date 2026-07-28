package com.example.flowpay

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response
import com.example.flowpay.screens.JornadaRequest
import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

object Constants {
    const val BASE_URL = "https://flowpay-backend-4xwj.onrender.com/api/"
}

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

interface FlowPayApiService {
    @POST("auth/login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/registrar")
    suspend fun registrarUsuario(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/google")
    suspend fun loginConGoogle(@Body request: GoogleLoginRequest): Response<LoginResponse>

    @POST("auth/forgot-password")
    suspend fun solicitarRecuperacion(@Body request: SolicitarRecuperacionRequest): Response<GenericResponse>

    @POST("auth/verify-code")
    suspend fun verificarCodigo(@Body request: VerificarCodigoRequest): Response<GenericResponse>

    @POST("auth/reset-password")
    suspend fun restablecerContrasena(@Body request: RestablecerPasswordRequest): Response<GenericResponse>

    @GET("auth/verificar-proveedor")
    suspend fun verificarProveedor(@Query("correo") correo: String): Response<ProveedorResponse>

    @POST("producto/crear")
    suspend fun crearProducto(@Body request: ProductRequest): Response<GenericProductResponse>

    // 🟢 CORREGIDO: Apunta a /activos/ para no traer el historial antiguo repetido
    @GET("producto/activos/{usuario_id}")
    suspend fun obtenerProductosPorUsuario(
        @Path("usuario_id") usuarioId: Int
    ): Response<CatalogResponse>

    @POST("jornada/abrir")
    suspend fun abrirJornada(@Body request: JornadaRequest): Response<GenericJornadaResponse>

    @PUT("jornada/cerrar")
    suspend fun cerrarJornada(@Body request: CerrarJornadaRequest): Response<GenericJornadaResponse>

    @POST("venta/registrar")
    suspend fun registrarVenta(@Body request: VentaRequest): Response<GenericVentaResponse>

    @POST("encuesta/registrar")
    suspend fun registrarEncuesta(@Body request: EncuestaRequest): Response<GenericEncuestaResponse>
}

data class LoginRequest(val correo: String, val contrasena: String)
data class GoogleLoginRequest(val idToken: String)

data class ProveedorResponse(
    val ok: Boolean,
    val esGoogle: Boolean
)

data class SolicitarRecuperacionRequest(val correo: String)
data class RestablecerPasswordRequest(val token: String, val nuevaContrasena: String)

data class VerificarCodigoRequest(val correo: String, val codigoIngresado: String)

data class GenericResponse(
    val ok: Boolean,
    val msg: String,
    val token: String? = null
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

data class CatalogResponse(
    val ok: Boolean,
    val productos: List<ProductoResponse>?
)

data class ProductoResponse(
    val id: Int,
    val nombre: String,
    @SerializedName("precio")
    val precio_venta: Double? = null,
    val precio_unitario: Double? = null
) {
    val precioFinal: Double
        get() = precio_venta ?: precio_unitario ?: 0.0
}

@Serializable
data class GenericProductResponse(
    val ok: Boolean,
    val msg: String? = null,
    val id: Int? = null,
    val producto_id: Int? = null
)

data class GenericJornadaResponse(
    val ok: Boolean,
    val msg: String?,
    val jornada_id: Int?
)

data class EncuestaRequest(
    val jornada_id: Int,
    val id_usuario: Int? = null,
    val pregunta_1: Int? = null,
    val pregunta_2: Int? = null,
    val pregunta_3: Int? = null,
    val puntuacion_app: Int? = null, // Mantenemos como respaldo
    val comentarios: String? = null
)

data class GenericEncuestaResponse(
    val ok: Boolean,
    val msg: String?
)

data class VentaRequest(
    val jornada_id: Int,
    val producto_id: Int? = null,
    val cantidad: Int? = 1,
    val precio_unitario: Double? = null,
    val total: Double,
    @SerializedName("tipo_pago")
    val tipo_pago: String,
    @SerializedName("metodo_pago")
    val metodo_pago: String = tipo_pago,
    @SerializedName("detalles")
    val detalles: List<DetalleVentaRequest>? = null,
    @SerializedName("productos")
    val productos: List<DetalleVentaRequest>? = detalles
)

data class DetalleVentaRequest(
    val producto_id: Int,
    val cantidad: Int = 1,
    @SerializedName("precio_unitario")
    val precio_unitario: Double,
    @SerializedName("precio")
    val precio: Double = precio_unitario
)

data class GenericVentaResponse(
    val ok: Boolean,
    val msg: String?,
    val venta_id: Int?
)