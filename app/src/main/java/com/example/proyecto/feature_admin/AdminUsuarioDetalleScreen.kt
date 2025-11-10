package com.example.proyecto.feature_admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.FakeDataSource
import com.example.proyecto.data.SessionManager
import com.example.proyecto.domain.DiaSemana
import com.example.proyecto.domain.TurnoDia
import com.example.proyecto.domain.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuarioDetalleScreen(
    username: String,
    onBack: () -> Unit
) {
    val todasLasMesas = FakeDataSource.mesas

    val usuarioOriginal: Usuario? = SessionManager.usuarios.find { it.username == username }

    var mesasSeleccionadas by remember {
        mutableStateOf(usuarioOriginal?.mesasAsignadas?.toSet() ?: emptySet())
    }
    var horarioPorDia by remember {
        mutableStateOf(
            usuarioOriginal?.horarioSemanal?.associate { it.dia to it.horario }
                ?: DiaSemana.values().associateWith { "OFF" }
        )
    }

    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuario: $username") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("<")
                    }
                }
            )
        }
    ) { padding ->
        if (usuarioOriginal == null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text("Usuario no encontrado")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    Text("Nombre: ${usuarioOriginal.nombreCompleto}")
                    Text("Rol: ${usuarioOriginal.rol}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Mesas asignadas:")
                }

                items(todasLasMesas) { mesa ->
                    val checked = mesasSeleccionadas.contains(mesa.id)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                mesasSeleccionadas = if (isChecked) {
                                    mesasSeleccionadas + mesa.id
                                } else {
                                    mesasSeleccionadas - mesa.id
                                }
                            }
                        )
                        Text("Mesa ${mesa.id} - ${mesa.nombre}")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Horario semanal:")
                }

                items(DiaSemana.values()) { dia ->
                    val horario = horarioPorDia[dia] ?: "OFF"
                    val esOff = horario.uppercase() == "OFF"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = dia.name.lowercase().replaceFirstChar { it.uppercase() },
                            modifier = Modifier.width(90.dp)
                        )
                        Checkbox(
                            checked = esOff,
                            onCheckedChange = { off ->
                                horarioPorDia = if (off) {
                                    horarioPorDia + (dia to "OFF")
                                } else {
                                    horarioPorDia + (dia to "")
                                }
                            }
                        )
                        Text("OFF")
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = if (esOff) "" else horario,
                            onValueChange = { nuevo ->
                                horarioPorDia = horarioPorDia + (dia to nuevo)
                            },
                            enabled = !esOff,
                            label = { Text("Horario") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val nuevoHorario = DiaSemana.values().map { dia ->
                                val hor = horarioPorDia[dia] ?: "OFF"
                                TurnoDia(dia = dia, horario = if (hor.isBlank()) "OFF" else hor)
                            }
                            val actualizado = usuarioOriginal.copy(
                                mesasAsignadas = mesasSeleccionadas.toList().sorted(),
                                horarioSemanal = nuevoHorario
                            )
                            SessionManager.actualizarUsuario(actualizado)
                            mensaje = "Cambios guardados"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar cambios")
                    }

                    if (mensaje != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = mensaje ?: "", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

        }
}
    }
