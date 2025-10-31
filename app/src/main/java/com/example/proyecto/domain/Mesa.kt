package com.example.proyecto.domain

data class Mesa(
    val id: Int,
    val nombre: String,
    val estado: MesaEstado,
    val mesero: String? = null
)

enum class MesaEstado {
    LIBRE, OCUPADA, CUENTA
}