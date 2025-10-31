package com.example.proyecto.feature_orden

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.domain.OrdenItem
import com.example.proyecto.ui.theme.GreenPrimary
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenScreen(
    mesaId: Int,
    onBack: () -> Unit,
    onAgregarProducto: () -> Unit
) {

    val orden = ComanderoRepository.getOrdenDeMesa(mesaId)
    val total = orden.items.sumOf { it.producto.precio * it.cantidad }

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
                Text(
                    text = "Total: $${"%.2f".format(total)}",
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )
                Button(
                    onClick = { /* TODO: enviar */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(text = "Enviar", color = GreenPrimary)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
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
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "${item.cantidad} x ${item.producto.nombre}")
            Text(text = "$${item.producto.precio} c/u")
        }
        Text(text = "$${item.producto.precio * item.cantidad}")
    }
}
