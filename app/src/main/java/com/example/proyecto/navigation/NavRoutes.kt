package com.example.proyecto.navigation

object NavRoutes {
    const val LOGIN = "login"
    const val MESAS = "mesas"
    const val MENU = "menu/{mesaId}"
    const val ORDEN = "orden/{mesaId}"
    const val PERFIL = "perfil"
    const val ADMIN_USERS = "admin_usuarios"

    fun menu(mesaId: Int) = "menu/$mesaId"
    fun orden(mesaId: Int) = "orden/$mesaId"
}
