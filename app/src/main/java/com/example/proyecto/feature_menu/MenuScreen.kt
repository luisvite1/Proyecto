package com.example.proyecto.feature_menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.FakeDataSource
import com.example.proyecto.domain.Producto

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
                    ProductoRow(
                        producto = prod,
                        onAdd = {
                            // aquí ya puedes usar mesaId si quieres registrar
                            onProductoSeleccionado(prod)
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
    onAdd: () -> Unit
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
        IconButton(onClick = onAdd) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar"
            )
        }
    }
}
