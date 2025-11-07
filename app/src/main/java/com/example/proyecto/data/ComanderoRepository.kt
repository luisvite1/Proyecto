package com.example.proyecto.data

import com.example.proyecto.domain.*

/**
 * Repositorio en memoria.
 * Para este ejemplo es singleton (object) para que todas las pantallas compartan el mismo estado.
 */
object ComanderoRepository {

    // “BD” en memoria
    private val ordenesPorMesa = mutableMapOf<Int, Orden>()

    fun getCategorias(): List<Categoria> = FakeDataSource.categorias

    fun getProductosPorCategoria(categoriaId: String): List<Producto> =
        FakeDataSource.productos.filter { it.categoriaId == categoriaId }

    fun getMesas(): List<Mesa> = FakeDataSource.mesas

    fun getOrdenDeMesa(mesaId: Int): Orden =
        ordenesPorMesa[mesaId] ?: Orden(mesaId = mesaId)

    fun agregarProductoAMesa(mesaId: Int, producto: Producto) {
        val ordenActual = ordenesPorMesa[mesaId] ?: Orden(mesaId)
        // si ya existe el producto, aumentamos cantidad
        val existente = ordenActual.items.find { it.producto.id == producto.id }
        val nuevosItems = if (existente != null) {
            ordenActual.items.map {
                if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
            }
        } else {
            ordenActual.items + OrdenItem(producto = producto, cantidad = 1)
        }
        ordenesPorMesa[mesaId] = ordenActual.copy(items = nuevosItems)
        // actualizar estado de mesa a OCUPADA
        actualizarEstadoMesa(mesaId, MesaEstado.OCUPADA)
    }

    fun quitarProductoDeMesa(mesaId: Int, productoId: String) {
        val ordenActual = ordenesPorMesa[mesaId] ?: return
        val nuevos = ordenActual.items
            .mapNotNull { item ->
                if (item.producto.id == productoId) {
                    val nuevaCant = item.cantidad - 1
                    if (nuevaCant <= 0) null else item.copy(cantidad = nuevaCant)
                } else item
            }
        ordenesPorMesa[mesaId] =
            if (nuevos.isEmpty()) ordenActual.copy(items = emptyList()) else ordenActual.copy(items = nuevos)
    }

    fun marcarCuenta(mesaId: Int) {
        actualizarEstadoMesa(mesaId, MesaEstado.CUENTA)
    }

    private fun actualizarEstadoMesa(mesaId: Int, estado: MesaEstado) {
        val idx = FakeDataSource.mesas.indexOfFirst { it.id == mesaId }
        if (idx != -1) {
            val mesa = FakeDataSource.mesas[idx]
            FakeDataSource.mesas[idx] = mesa.copy(estado = estado)
        }
    }

    fun cerrarMesa(mesaId: Int) {
        ordenesPorMesa.remove(mesaId)
        actualizarEstadoMesa(mesaId, MesaEstado.LIBRE)
    }
    fun enviarOrden(mesaId: Int) {
        val ordenActual = ordenesPorMesa[mesaId] ?: return
        if (ordenActual.items.isEmpty()) return

        // Subtotal de lo que se está enviando a cocina
        val subtotal = ordenActual.items.sumOf { it.producto.precio * it.cantidad }

        // Limpiamos items pendientes pero acumulamos el total
        ordenesPorMesa[mesaId] = ordenActual.copy(
            items = emptyList(),
            totalAcumulado = ordenActual.totalAcumulado + subtotal
        )
    }



}


