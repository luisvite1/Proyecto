package com.example.proyecto.feature_perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.RolUsuario
import java.time.LocalDate
import java.time.DayOfWeek
import com.example.proyecto.domain.DiaSemana
import com.example.proyecto.domain.TurnoDia
import com.example.proyecto.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onBack: () -> Unit,
    onAdminUsuarios: () -> Unit,
    onRegistrarUsuario: () -> Unit,
    onAdminMesas: () -> Unit,
    onCerrarSesionTodos: () -> Unit
) {
    val user = SessionManager.currentUser
    val hoyDiaSemana = remember {
        when (LocalDate.now().dayOfWeek) {
            DayOfWeek.MONDAY -> DiaSemana.LUNES
            DayOfWeek.TUESDAY -> DiaSemana.MARTES
            DayOfWeek.WEDNESDAY -> DiaSemana.MIERCOLES
            DayOfWeek.THURSDAY -> DiaSemana.JUEVES
            DayOfWeek.FRIDAY -> DiaSemana.VIERNES
            DayOfWeek.SATURDAY -> DiaSemana.SABADO
            DayOfWeek.SUNDAY -> DiaSemana.DOMINGO
        }
    }

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
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
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

                    Text(text = "Horario semanal:")

                    user?.horarioSemanal?.forEach { turno ->
                        val esHoy = turno.dia == hoyDiaSemana
                        val esOff = turno.horario.uppercase() == "OFF"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    if (esHoy) Color(0xFFE8F5E9) else Color.Transparent
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = turno.dia.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                modifier = Modifier.weight(1f),
                                color = if (esOff) Color.Gray else Color.Unspecified
                            )
                            Text(
                                text = if (esOff) "OFF" else turno.horario,
                                color = if (esOff) Color.Gray else Color.Black
                            )
                        }
                    }

                    user?.let {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Mesas asignadas: ${it.mesasAsignadas.joinToString(", ")}"
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        if (it.rol == RolUsuario.ADMIN) {
                            Button(
                                onClick = onRegistrarUsuario,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Registrar usuario")
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = onAdminUsuarios,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Administrar usuarios")
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = onAdminMesas,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Control de mesas")
                            }
                            Spacer(modifier = Modifier.height(8.dp))

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
    }
}
