package com.example.proyecto.domain

data class Orden(
    val mesaId: Int,
    val items: List<OrdenItem> = emptyList(),
    val enviadoACocina: Boolean = false
)