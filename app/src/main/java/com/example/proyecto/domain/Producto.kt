package com.example.proyecto.domain

data class Producto(
    val id: String,
    val nombre: String,
    val categoriaId: String,
    val precio: Double,
    val disponible: Boolean = true,
    val nota: String? = null
)