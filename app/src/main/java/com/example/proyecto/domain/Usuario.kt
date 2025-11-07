package com.example.proyecto.domain
enum class RolUsuario {
    ADMIN,
    MESERO,
    BARTENDER
}

data class Usuario(
    val username: String,
    val password: String,
    val nombreCompleto: String,
    val rol: RolUsuario,
    val mesasAsignadas: List<Int>
)
