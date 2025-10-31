package com.example.proyecto.feature_menu

import androidx.lifecycle.ViewModel
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.domain.Categoria
import com.example.proyecto.domain.Producto

class MenuViewModel(
    private val repo: ComanderoRepository = ComanderoRepository
) : ViewModel() {

    fun getCategorias(): List<Categoria> = repo.getCategorias()

    fun getProductos(categoriaId: String): List<Producto> =
        repo.getProductosPorCategoria(categoriaId)
}
