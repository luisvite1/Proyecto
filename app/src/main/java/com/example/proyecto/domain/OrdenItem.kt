package com.example.proyecto.domain

data class OrdenItem(
    val producto: Producto,
    val cantidad: Int,
    val nota: String? = null
)
