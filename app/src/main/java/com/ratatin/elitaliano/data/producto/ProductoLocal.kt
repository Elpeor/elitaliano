package com.ratatin.elitaliano.data.producto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "producto")
data class ProductoLocal(
    @PrimaryKey(autoGenerate = true) val idProducto: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagen : String
)