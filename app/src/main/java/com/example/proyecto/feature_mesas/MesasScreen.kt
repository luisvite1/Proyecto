package com.example.proyecto.feature_mesas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.FakeDataSource
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.Mesa
import com.example.proyecto.domain.MesaEstado
import com.example.proyecto.domain.RolUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MesasScreen(
    onMesaSelected: (Int) -> Unit,
    onPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onTerminos: () -> Unit
) {
    val currentUser = SessionManager.currentUser
    val todasLasMesas = remember { FakeDataSource.mesas }

    val mesas: List<Mesa> = when (currentUser?.rol) {
        RolUsuario.ADMIN -> todasLasMesas
        else -> {
            val asignadas = currentUser?.mesasAsignadas ?: emptyList()
            todasLasMesas.filter { it.id in asignadas }
        }
    }

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentUser?.nombreCompleto ?: "Mesas"
                    )
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Perfil") },
                            onClick = {
                                menuExpanded = false
                                onPerfil()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                menuExpanded = false
                                onCerrarSesion()
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Términos y condiciones") },
                            onClick = {
                                menuExpanded = false
                                onTerminos()
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            items(mesas) { mesa ->
                MesaCard(
                    mesa = mesa,
                    onClick = { onMesaSelected(mesa.id) }
                )
            }
        }
    }
}

@Composable
private fun MesaCard(
    mesa: Mesa,
    onClick: () -> Unit
) {
    val fondo = when (mesa.estado) {
        MesaEstado.LIBRE -> Color(0xFFE8F5E9)
        MesaEstado.OCUPADA -> Color(0xFFFFF3E0)
        MesaEstado.CUENTA -> Color(0xFFFFEBEE)
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = fondo
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = mesa.nombre)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mesa.estado.name,
                color = Color.Gray
            )
        }
    }
}
