package com.ratatin.elitaliano.dataSQL


data class Producto(
    val idProducto: Long? = null,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagen : String
)
