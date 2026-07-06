package com.example.flowpay

import kotlinx.serialization.Serializable // Clave para la conversión automática de JSON

// --- JORNADA ENDPOINTS ---
@Serializable
data class AbrirJornadaRequest(
    val usuario_id: Int,
    val monto_inversion: Double
)

@Serializable
data class EstadoJornadaResponse(
    val jornada_id: Int? = null,           // 🟢 Editado: Evita caídas si la jornada no está activa (viene nula)
    val estado: String,
    val monto_inversion: Double? = null    // 🟢 Editado: Soporta valores nulos por defecto sin romper Ktor
)

@Serializable
data class CerrarJornadaRequest(
    val jornada_id: Int,
    val monto_inversion: Double,
    val monto_ventas_efectivo: Double,
    val monto_ventas_transferencia: Double,
    val ganancia_neta: Double,
    val encuesta_contestada: Int
)

// --- PRODUCTO ENDPOINTS ---
@Serializable
data class CrearProductoRequest(
    val usuario_id: Int,
    val nombre: String,
    val precio: Double
)

@Serializable
data class ActualizarEstadoProductoRequest(
    val id: Int,
    val activo: Int // 1 para activar, 0 para desactivar
)

// --- ENCUESTA ENDPOINTS ---
@Serializable
data class RegistrarEncuestaRequest(
    val jornada_id: Int,
    val puntuacion_app: Int,
    val comentarios: String? = null // Soporta comentarios vacíos de los usuarios
)