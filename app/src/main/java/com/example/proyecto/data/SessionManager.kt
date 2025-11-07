package com.example.proyecto.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.proyecto.domain.RolUsuario
import com.example.proyecto.domain.Usuario

object SessionManager {

    // Usuario logueado actualmente
    var currentUser by mutableStateOf<Usuario?>(null)
        private set

    // Lista de usuarios en memoria (temporal)
    val usuarios = mutableStateListOf<Usuario>()

    init {
        // Usuarios de ejemplo
        usuarios.addAll(
            listOf(
                Usuario(
                    username = "admin",
                    password = "admin",
                    nombreCompleto = "Administrador",
                    rol = RolUsuario.ADMIN,
                    mesasAsignadas = listOf(1, 2, 3, 4)
                ),
                Usuario(
                    username = "angel",
                    password = "1234",
                    nombreCompleto = "Ángel Meneses",
                    rol = RolUsuario.MESERO,
                    mesasAsignadas = listOf(1, 2, 3)
                ),
                Usuario(
                    username = "juan",
                    password = "1234",
                    nombreCompleto = "Juan Pérez",
                    rol = RolUsuario.BARTENDER,
                    mesasAsignadas = listOf(4)
                )
            )
        )
    }

    fun login(username: String, password: String): Boolean {
        val user = usuarios.find { it.username == username && it.password == password }
        currentUser = user
        return user != null
    }

    fun logout() {
        currentUser = null
    }

    fun logoutAllDevices() {
        // En un back-end real cerrarías todas las sesiones;
        // aquí solo limpiamos la sesión local
        currentUser = null
    }

    fun agregarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }
}
