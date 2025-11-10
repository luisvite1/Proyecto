package com.example.proyecto.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.proyecto.domain.DiaSemana
import com.example.proyecto.domain.RolUsuario
import com.example.proyecto.domain.TurnoDia
import com.example.proyecto.domain.Usuario

object SessionManager {

    // Usuario logueado actualmente
    var currentUser by mutableStateOf<Usuario?>(null)
        private set

    // Lista de usuarios en memoria (temporal)
    val usuarios = mutableStateListOf<Usuario>()

    init {
        usuarios.addAll(
            listOf(
                Usuario(
                    username = "admin",
                    password = "admin",
                    nombreCompleto = "Administrador",
                    rol = RolUsuario.ADMIN,
                    mesasAsignadas = listOf(1, 2, 3, 4, 5, 6),
                    horarioSemanal = horarioOffSemana()
                ),
                Usuario(
                    username = "angel",
                    password = "1234",
                    nombreCompleto = "Ángel Meneses",
                    rol = RolUsuario.MESERO,
                    mesasAsignadas = listOf(1, 2, 3),
                    horarioSemanal = horarioVespertinoSemana()
                ),
                Usuario(
                    username = "juan",
                    password = "1234",
                    nombreCompleto = "Juan Pérez",
                    rol = RolUsuario.BARTENDER,
                    mesasAsignadas = listOf(4, 5, 6),
                    horarioSemanal = horarioVespertinoSemana()
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

    private fun horarioOffSemana(): List<TurnoDia> =
        DiaSemana.values().map { dia ->
            TurnoDia(dia = dia, horario = "OFF")
        }

    private fun horarioVespertinoSemana(): List<TurnoDia> =
        DiaSemana.values().map { dia ->
            when (dia) {
                DiaSemana.LUNES,
                DiaSemana.MARTES,
                DiaSemana.MIERCOLES,
                DiaSemana.JUEVES,
                DiaSemana.VIERNES -> TurnoDia(dia, "18:00 - 00:00")
                DiaSemana.SABADO,
                DiaSemana.DOMINGO -> TurnoDia(dia, "OFF")
            }
        }
    fun actualizarUsuario(actualizado: Usuario) {
        val idx = usuarios.indexOfFirst { it.username == actualizado.username }
        if (idx != -1) {
            usuarios[idx] = actualizado
        }
        if (currentUser?.username == actualizado.username) {
            currentUser = actualizado
        }
    }

    fun removerMesaDeUsuarios(mesaId: Int) {
        for (i in usuarios.indices) {
            val u = usuarios[i]
            if (mesaId in u.mesasAsignadas) {
                usuarios[i] = u.copy(
                    mesasAsignadas = u.mesasAsignadas.filter { it != mesaId }
                )
            }
        }
    }


}
