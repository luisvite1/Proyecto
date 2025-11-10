package com.example.proyecto.feature_admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(
    onBack: () -> Unit,
    onUserClick: (String) -> Unit
) {
    val usuarios = SessionManager.usuarios

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios registrados") },
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
            usuarios.forEach { user ->
                TextButton(
                    onClick = { onUserClick(user.username) }
                ) {
                    Text("${user.username} - ${user.nombreCompleto} (${user.rol})")
                }
                Divider()
            }
        }
    }
}
