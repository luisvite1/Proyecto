package com.example.proyecto.data

import com.example.proyecto.domain.*

object FakeDataSource {

    // categorías actuales del bar
    val categorias = listOf(
        Categoria("litros", "Litros"),
        Categoria("copeo", "Copeo"),
        Categoria("bebidas", "Bebidas"),
        Categoria("comida", "Comida")
    )

    // productos ejemplo
    val productos = listOf(
        Producto("p1", "Litro de mojito", "litros", 120.0),
        Producto("p2", "Litro de piña colada", "litros", 130.0),

        Producto("p3", "Whisky (copeo)", "copeo", 85.0),
        Producto("p4", "Vodka (copeo)", "copeo", 70.0),

        Producto("p5", "Refresco", "bebidas", 25.0),
        Producto("p6", "Agua natural", "bebidas", 15.0),

        Producto("p7", "Boneless", "comida", 90.0),
        Producto("p8", "Hamburguesa", "comida", 110.0),
    )

    // mesas iniciales
    val mesas = mutableListOf(
        Mesa(1, "Mesa 1", MesaEstado.LIBRE),
        Mesa(2, "Mesa 2", MesaEstado.OCUPADA),
        Mesa(3, "Mesa 3", MesaEstado.CUENTA),
        Mesa(4, "Terraza 1", MesaEstado.LIBRE),
    )
}