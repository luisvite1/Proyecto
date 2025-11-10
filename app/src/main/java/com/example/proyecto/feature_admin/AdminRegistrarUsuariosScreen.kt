package com.example.proyecto.feature_admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.RolUsuario
import com.example.proyecto.domain.Usuario
import com.example.proyecto.domain.DiaSemana
import com.example.proyecto.domain.TurnoDia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRegistrarUsuarioScreen(
    onBack: () -> Unit
) {
    var nombreCompleto by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf(RolUsuario.MESERO) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar usuario") },
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

            Text("Rol:")
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
                    if (nombreCompleto.isNotBlank() &&
                        username.isNotBlank() &&
                        password.isNotBlank()
                    ) {
                        // Horario y mesas vacíos por ahora
                        val nuevo = Usuario(
                            username = username.trim(),
                            password = password.trim(),
                            nombreCompleto = nombreCompleto.trim(),
                            rol = rolSeleccionado,
                            mesasAsignadas = emptyList(),
                            horarioSemanal = DiaSemana.values().map { dia ->
                                TurnoDia(dia, "OFF")
                            }
                        )
                        SessionManager.agregarUsuario(nuevo)
                        mensaje = "Usuario registrado correctamente"

                        nombreCompleto = ""
                        username = ""
                        password = ""
                    } else {
                        mensaje = "Completa los datos correctamente"
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
        }
    }
}
