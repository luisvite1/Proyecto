package com.example.proyecto.domain

data class Categoria(
    val id: String,          // "litros", "copeo", "bebidas", "comida"
    val nombre: String,      // "Litros", "Copeo", "Bebidas", "Comida"
    val icon: String? = null // luego si quieres ícono
)