package com.example.proyecto.feature_menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.data.FakeDataSource
import com.example.proyecto.domain.Producto
import com.example.proyecto.ui.theme.GreenPrimary

@Composable
@androidx.compose.material3.ExperimentalMaterial3Api
fun MenuScreen(
    mesaId: Int,                                  // 👈 ahora sí lo recibe
    onProductoSeleccionado: (Producto) -> Unit,
    onBack: () -> Unit
) {
    val categorias = FakeDataSource.categorias

    // si no hay categorías, mostramos pantalla vacía
    if (categorias.isEmpty()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Menú - Mesa $mesaId") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay categorías")
            }
        }
        return
    }

    // categoría seleccionada
    var categoriaSeleccionadaId by remember {
        mutableStateOf(categorias.first().id)
    }

    // productos filtrados
    val productos = FakeDataSource.productos.filter {
        it.categoriaId == categoriaSeleccionadaId
    }
    val cantidades = remember {
        mutableStateMapOf<String, Int>()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú - Mesa $mesaId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = GreenPrimary,
                contentColor = Color.White
            ) {
                val totalSeleccionado = productos.sumOf { prod ->
                    (cantidades[prod.id] ?: 0) * prod.precio
                }

                Text(
                    text = "Selección: $${"%.2f".format(totalSeleccionado)}",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                Button(
                    onClick = {
                        // por cada producto y cantidad, lo añadimos a la mesa
                        productos.forEach { prod ->
                            val cant = cantidades[prod.id] ?: 0
                            repeat(cant) {
                                ComanderoRepository.agregarProductoAMesa(mesaId, prod)
                                // si quieres, puedes seguir llamando el callback
                                onProductoSeleccionado(prod)
                            }
                        }
                        // limpiamos selección
                        cantidades.clear()
                        // regresamos a la pantalla de la orden
                        onBack()
                    },
                    enabled = cantidades.values.any { it > 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text("Aceptar", color = GreenPrimary)
                }
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // CATEGORÍAS
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(categorias) { cat ->
                    FilterChip(
                        selected = cat.id == categoriaSeleccionadaId,
                        onClick = { categoriaSeleccionadaId = cat.id },
                        label = { Text(cat.nombre) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // PRODUCTOS
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(productos) { prod ->
                    val cantidadActual = cantidades[prod.id] ?: 0

                    ProductoRow(
                        producto = prod,
                        cantidad = cantidadActual,
                        onIncrement = {
                            val nueva = cantidadActual + 1
                            cantidades[prod.id] = nueva
                        },
                        onDecrement = {
                            val nueva = (cantidadActual - 1).coerceAtLeast(0)
                            if (nueva == 0) {
                                cantidades.remove(prod.id)
                            } else {
                                cantidades[prod.id] = nueva
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun ProductoRow(
    producto: Producto,
    cantidad: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = producto.nombre)
            Text(text = "$${producto.precio}")
        }
        if (cantidad == 0) {
            IconButton(onClick = onIncrement) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar"
                )
            }
        } else {
            // Ya hay cantidad -> mostramos "- 1 +"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrement) {
                    Text(text = "-")
                }
                Text(
                    text = cantidad.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onIncrement) {
                    Text(text = "+")
                }
            }
        }
    }
}
