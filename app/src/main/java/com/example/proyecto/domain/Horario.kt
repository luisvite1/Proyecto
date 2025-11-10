package com.example.proyecto.domain

enum class DiaSemana {
    LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
}

data class TurnoDia(
    val dia: DiaSemana,
    val horario: String   // ej: "18:00 - 00:00" o "OFF"
)
