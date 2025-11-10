package com.example.proyecto.feature_admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.data.FakeDataSource
import com.example.proyecto.domain.Mesa
import com.example.proyecto.domain.MesaEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMesasScreen(
    onBack: () -> Unit
) {
    var nombreMesa by remember { mutableStateOf("") }
    var capacidadMesa by remember { mutableStateOf("") }
    var ubicacionMesa by remember { mutableStateOf("") }

    // Lista observable para forzar recomposición cuando cambian las mesas
    var mesas by remember { mutableStateOf(FakeDataSource.mesas.toList()) }

    // Mesa que se está intentando eliminar
    var mesaAEliminar by remember { mutableStateOf<Mesa?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Control de mesas") },
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
                value = nombreMesa,
                onValueChange = { nombreMesa = it },
                label = { Text("Nombre de la nueva mesa") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = capacidadMesa,
                onValueChange = { capacidadMesa = it },
                label = { Text("Capacidad (personas)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ubicacionMesa,
                onValueChange = { ubicacionMesa = it },
                label = { Text("Ubicación (ej: Interior, Terraza)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val cap = capacidadMesa.toIntOrNull()
                    if (
                        nombreMesa.isNotBlank() &&
                        cap != null &&
                        cap > 0 &&
                        ubicacionMesa.isNotBlank()
                    ) {
                        ComanderoRepository.agregarMesa(
                            nombre = nombreMesa.trim(),
                            capacidad = cap,
                            ubicacion = ubicacionMesa.trim()
                        )
                        // Refrescamos la lista local
                        mesas = FakeDataSource.mesas.toList()

                        // Limpiamos campos
                        nombreMesa = ""
                        capacidadMesa = ""
                        ubicacionMesa = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar mesa")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Mesas existentes:")

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(mesas) { mesa ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${mesa.id} - ${mesa.nombre}")
                            Text(text = "Capacidad: ${mesa.capacidad}")
                            Text(text = "Ubicación: ${mesa.ubicacion}")
                            Text(text = "Estado: ${mesa.estado}")
                        }

                        val puedeEliminar = mesa.estado == MesaEstado.LIBRE

                        Button(
                            onClick = {
                                if (puedeEliminar) {
                                    // Abrimos diálogo de confirmación
                                    mesaAEliminar = mesa
                                }
                            },
                            enabled = puedeEliminar
                        ) {
                            Text("Eliminar")
                        }
                    }
                    Divider()
                }
            }
        }

        // 🔔 Diálogo de confirmación para eliminar mesa
        mesaAEliminar?.let { mesa ->
            AlertDialog(
                onDismissRequest = { mesaAEliminar = null },
                title = { Text("Eliminar mesa") },
                text = {
                    Text("¿Seguro que quieres eliminar la mesa \"${mesa.nombre}\" (ID: ${mesa.id})?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            ComanderoRepository.eliminarMesa(mesa.id)
                            // Refrescamos lista local para ver el cambio al instante
                            mesas = FakeDataSource.mesas.toList()
                            mesaAEliminar = null
                        }
                    ) {
                        Text("Sí, eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mesaAEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
