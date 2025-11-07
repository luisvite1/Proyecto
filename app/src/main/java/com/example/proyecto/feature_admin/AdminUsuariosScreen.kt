package com.example.proyecto.feature_admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.RolUsuario
import com.example.proyecto.domain.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(
    onBack: () -> Unit
) {
    var nombreCompleto by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mesasTexto by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf(RolUsuario.MESERO) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar usuarios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("<")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mesasTexto,
                onValueChange = { mesasTexto = it },
                label = { Text("Mesas asignadas (ej: 1,2,3)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // selector simple de rol
            Row {
                RolUsuario.values().filter { it != RolUsuario.ADMIN }.forEach { rol ->
                    FilterChip(
                        selected = rolSeleccionado == rol,
                        onClick = { rolSeleccionado = rol },
                        label = { Text(rol.name) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val mesas = mesasTexto.split(",")
                        .mapNotNull { it.trim().toIntOrNull() }
                    if (nombreCompleto.isNotBlank() &&
                        username.isNotBlank() &&
                        password.isNotBlank() &&
                        mesas.isNotEmpty()
                    ) {
                        val nuevo = Usuario(
                            username = username,
                            password = password,
                            nombreCompleto = nombreCompleto,
                            rol = rolSeleccionado,
                            mesasAsignadas = mesas
                        )
                        SessionManager.agregarUsuario(nuevo)
                        mensaje = "Usuario agregado correctamente"

                        nombreCompleto = ""
                        username = ""
                        password = ""
                        mesasTexto = ""
                    } else {
                        mensaje = "Completa los datos y mesas correctamente"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar usuario")
            }

            if (mensaje != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensaje ?: "", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Usuarios registrados:")
            SessionManager.usuarios.forEach { user ->
                Text("- ${user.username} (${user.rol}) → Mesas: ${user.mesasAsignadas.joinToString(", ")}")
            }
        }
    }
}
