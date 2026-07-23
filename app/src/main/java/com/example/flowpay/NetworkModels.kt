package com.example.flowpay

import kotlinx.serialization.Serializable

@Serializable
data class AbrirJornadaRequest(
    val usuario_id: Int,
    val monto_inversion: Double
)

@Serializable
data class EstadoJornadaResponse(
    val jornada_id: Int? = null,
    val estado: String,
    val monto_inversion: Double? = null
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


@Serializable
data class ActualizarEstadoProductoRequest(
    val id: Int,
    val activo: Int
)
@Serializable
data class ProductoNetwork(
    val id: Int,
    val usuario_id: Int? = null,
    val nombre: String,
    val precio: Double,
    val foto_url: String? = null,
    val activo: Int? = 1
)



