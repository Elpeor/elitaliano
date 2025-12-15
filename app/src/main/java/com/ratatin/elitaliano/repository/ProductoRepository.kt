package com.ratatin.elitaliano.repository

import com.ratatin.elitaliano.data.producto.ProductoDao
import com.ratatin.elitaliano.dataSQL.Producto
import com.ratatin.elitaliano.remote.RetrofitInstance

class ProductoRepository(private val dao: ProductoDao){


    suspend fun agregar(nombre:String, descripcion:String, precio: Double, categoria: String, imagen: String) {
        categoria.lowercase()
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(descripcion.isNotBlank()) { "La descripción no puede estar vacía" }
        require(precio >= 0){"El precio no puede ser negativo"}
        require(categoria.isNotBlank()) {"La categoría no puede estar vacía"}
        require(categoria in listOf("pizza", "pasta", "postre", "ensalada")) { "La categoría debe ser 'pizza', 'pasta', 'ensalada' o 'postre'" }
        RetrofitInstance.api.createProductos(Producto(nombre = nombre.trim(), descripcion = descripcion.trim(), precio = precio, categoria = categoria.trim(), imagen = imagen))
    }

    suspend fun actualizar(nombre:String, descripcion:String, precio: Double, categoria: String, imagen: String, id : Long?){
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(descripcion.isNotBlank()) { "La descripción no puede estar vacía" }
        require(precio >= 0){"El precio no puede ser negativo"}
        require(categoria.isNotBlank()) {"La categoría no puede estar vacía"}
        require(categoria in listOf("pizza", "pasta", "postre", "ensalada")) { "La categoría debe ser 'pizza', 'pasta', 'ensalada' o 'postre'" }
        RetrofitInstance.api.updateProducto(id, Producto(nombre = nombre.trim(), descripcion = descripcion.trim(), precio = precio, categoria = categoria.trim(), imagen = imagen))
    }

    suspend fun eliminarPorId(id: Long?) {
        RetrofitInstance.api.deleteProductoPorId(id)
    }

    suspend fun getProductos(): List<Producto>{
        return RetrofitInstance.api.getProductos()
    }

}