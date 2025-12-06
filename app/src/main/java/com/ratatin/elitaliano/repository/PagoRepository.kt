package com.ratatin.elitaliano.repository

import com.ratatin.elitaliano.data.usuario.UsuarioLocal
import com.ratatin.elitaliano.dataSQL.Pago
import com.ratatin.elitaliano.dataSQL.Producto
import com.ratatin.elitaliano.dataSQL.Usuario
import com.ratatin.elitaliano.remote.RetrofitInstance
import java.sql.Date

class PagoRepository {

    suspend fun getPagos(): List<Pago>{
        return RetrofitInstance.api.getPagos()
    }

    suspend fun getPagosByIdUsuario(id: Long?): List<Pago>{
        return RetrofitInstance.api.getPagosByIdUsuario(id)
    }

    suspend fun agregar(usuario: Usuario?, cant: Int, producto: Producto, numTransaccion: Long) {
        RetrofitInstance.api.createPagos(Pago(usuario = usuario, cant = cant, fecha = Date(System.currentTimeMillis()), producto = producto, numTransaccion = numTransaccion))
    }



}