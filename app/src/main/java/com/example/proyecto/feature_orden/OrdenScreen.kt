package com.example.proyecto.feature_orden

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.domain.OrdenItem
import com.example.proyecto.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenScreen(
    mesaId: Int,
    onBack: () -> Unit,
    onAgregarProducto: () -> Unit
) {
    // Estado local de la orden
    var orden by remember { mutableStateOf(ComanderoRepository.getOrdenDeMesa(mesaId)) }

    // Total de lo pendiente (lo que aún no se envía a cocina)
    val totalPendiente = orden.items.sumOf { it.producto.precio * it.cantidad }
    // Total actual de la mesa (todo lo enviado + lo pendiente)
    val totalActual = orden.totalAcumulado + totalPendiente

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mesa $mesaId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAgregarProducto) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar producto"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = GreenPrimary,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    // ENVIAR (a cocina) -> solo cuando hay productos pendientes
                    Button(
                        onClick = {
                            ComanderoRepository.enviarOrden(mesaId)
                            // Recargamos la orden (ahora con items vacíos y totalAcumulado sumado)
                            orden = ComanderoRepository.getOrdenDeMesa(mesaId)
                        },
                        enabled = orden.items.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Enviar",
                            color = GreenPrimary
                        )
                    }

                    // CERRAR MESA (ticket / cuenta) -> depende del total actual
                    Button(
                        onClick = {
                            ComanderoRepository.marcarCuenta(mesaId)
                        },
                        enabled = totalActual > 0.0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Cerrar mesa",
                            color = GreenPrimary
                        )
                    }

                    // MESA PAGADA (liberar mesa) -> depende del total actual
                    Button(
                        onClick = {
                            ComanderoRepository.cerrarMesa(mesaId)
                            onBack()
                        },
                        enabled = totalActual > 0.0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "Mesa pagada",
                            color = GreenPrimary
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            // Total actual de la mesa (arriba, pequeño)
            Text(
                text = "Total actual: $${"%.2f".format(totalActual)}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = Color.Gray
            )

            LazyColumn {
                items(orden.items) { item ->
                    OrdenItemRow(item)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun OrdenItemRow(item: OrdenItem) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "${item.cantidad} x ${item.producto.nombre}")
            Text(text = "$${item.producto.precio} c/u")
        }
        Text(text = "$${item.producto.precio * item.cantidad}")
    }
}
