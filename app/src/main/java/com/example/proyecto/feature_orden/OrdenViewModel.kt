package com.example.proyecto.feature_orden

import androidx.lifecycle.ViewModel
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.domain.Orden
import com.example.proyecto.domain.Producto

class OrdenViewModel(
    private val repo: ComanderoRepository = ComanderoRepository
) : ViewModel() {

    fun getOrden(mesaId: Int): Orden = repo.getOrdenDeMesa(mesaId)

    fun agregarProducto(mesaId: Int, producto: Producto) {
        repo.agregarProductoAMesa(mesaId, producto)
    }

    fun quitarProducto(mesaId: Int, productoId: String) {
        repo.quitarProductoDeMesa(mesaId, productoId)
    }

    fun marcarCuenta(mesaId: Int) {
        repo.marcarCuenta(mesaId)
    }

    fun cerrarMesa(mesaId: Int) {
        repo.cerrarMesa(mesaId)
    }
}
