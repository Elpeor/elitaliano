package com.ratatin.elitaliano.dataSQL

import com.ratatin.elitaliano.data.usuario.Usuario
import java.sql.Date

data class Pago(
    val idPago: Long? = null,
    val usuario: Usuario?,
    val cant: Int,
    val fecha: Date,
    val producto: Producto,
    val numTransaccion: Long
)
