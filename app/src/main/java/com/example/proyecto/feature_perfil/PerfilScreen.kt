package com.example.proyecto.feature_perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.RolUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onBack: () -> Unit,
    onAdminUsuarios: () -> Unit,
    onCerrarSesionTodos: () -> Unit
) {
    val user = SessionManager.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user?.nombreCompleto?.take(1) ?: "?",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = user?.nombreCompleto ?: "Sin usuario")
            Text(text = "Rol: ${user?.rol ?: "N/A"}")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Turno actual: Vespertino (18:00 - 00:00)")
            Text(text = "Turno semanal: Lun - Vie, Vespertino")
            Spacer(modifier = Modifier.height(24.dp))

            user?.let {
                Text(
                    text = "Mesas asignadas: ${it.mesasAsignadas.joinToString(", ")}"
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (it.rol == RolUsuario.ADMIN) {
                    Button(
                        onClick = onAdminUsuarios,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Administrar usuarios")
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onCerrarSesionTodos,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar sesión en todos los dispositivos")
                    }
                }
            }
        }
    }
}


