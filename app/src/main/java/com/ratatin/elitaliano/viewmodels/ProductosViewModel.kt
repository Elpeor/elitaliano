package com.ratatin.elitaliano.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratatin.elitaliano.dataSQL.Producto
import com.ratatin.elitaliano.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(val productoRepo: ProductoRepository) : ViewModel() {

    private val _carro = MutableStateFlow<List<Producto>>(emptyList())
    val carro: StateFlow<List<Producto>> = _carro

    fun agregarAlCarro(producto: Producto) {
        _carro.value = _carro.value + producto
    }

    fun eliminarUnidadDelCarro(producto: Producto) {
        val listaActual = _carro.value.toMutableList()
        val index = listaActual.indexOfFirst { it.idProducto == producto.idProducto }
        if (index != -1) {
            listaActual.removeAt(index)
            _carro.value = listaActual
        }
    }

    fun limpiarCarro() {
        _carro.value = emptyList()
    }

    fun totalCarro(): Double {
        return _carro.value.sumOf { it.precio }
    }

    private val _productoList = MutableLiveData<List<Producto>>(emptyList())

    val productoList : LiveData<List<Producto>> = _productoList

    init {
        fetchProductos()
        viewModelScope.launch {

        }
    }


    fun fetchProductos() {
        viewModelScope.launch {
            try {
                _productoList.value = productoRepo.getProductos()
            } catch (e: Exception){
                println("error al obtener datos: ${e.localizedMessage}")
            }

        }
    }


}