package com.example.proyecto.feature_mesas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyecto.data.FakeDataSource
import com.example.proyecto.domain.Mesa
import com.example.proyecto.domain.MesaEstado
import androidx.compose.material.icons.filled.Menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MesasScreen(
    onMesaSelected: (Int) -> Unit,
    onIrAlMenu: () -> Unit
) {
    val mesas = remember { FakeDataSource.mesas }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mesas") },
                actions = {
                    IconButton(onClick = onIrAlMenu) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú"
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
