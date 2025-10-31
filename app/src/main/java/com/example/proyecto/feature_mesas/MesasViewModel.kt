package com.example.proyecto.feature_mesas

import androidx.lifecycle.ViewModel
import com.example.proyecto.data.ComanderoRepository
import com.example.proyecto.domain.Mesa

class MesasViewModel(
    private val repo: ComanderoRepository = ComanderoRepository
) : ViewModel() {

    fun getMesas(): List<Mesa> = repo.getMesas()
}
