package com.example.proyecto.domain

data class Mesa(
    val id: Int,
    val nombre: String,
    val estado: MesaEstado,
    val mesero: String? = null,
    val capacidad: Int = 0,
    val ubicacion: String = ""
)


enum class MesaEstado {
    LIBRE, OCUPADA, CUENTA
}