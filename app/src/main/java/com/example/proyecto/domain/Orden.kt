package com.example.proyecto.domain

data class Orden(
    val mesaId: Int,
    val items: List<OrdenItem> = emptyList(),     // productos pendientes
    val enviadoACocina: Boolean = false,          // lo puedes dejar para futuro
    val totalAcumulado: Double = 0.0              // TODO lo que ya se ha pedido
)
