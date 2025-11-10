package com.example.proyecto.navigation

object NavRoutes {
    const val LOGIN = "login"
    const val MESAS = "mesas"
    const val MENU = "menu/{mesaId}"
    const val ORDEN = "orden/{mesaId}"
    const val PERFIL = "perfil"

    const val ADMIN_USERS = "admin_usuarios"              // 👉 listado de usuarios
    const val ADMIN_USER_DETAIL = "admin_usuario/{username}"  // 👉 detalle de un usuario
    const val ADMIN_REGISTRAR_USUARIO = "admin_registrar_usuario"
    const val ADMIN_MESAS = "admin_mesas"

    fun menu(mesaId: Int) = "menu/$mesaId"
    fun orden(mesaId: Int) = "orden/$mesaId"
    fun adminUsuarioDetalle(username: String) = "admin_usuario/$username"
}

