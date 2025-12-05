package com.ratatin.elitaliano.repository

import com.ratatin.elitaliano.data.usuario.Usuario
import com.ratatin.elitaliano.data.usuario.UsuarioDao
import com.ratatin.elitaliano.remote.RetrofitInstance

class UsuarioRepository(private val dao: UsuarioDao){

    suspend fun login(correo: String) = RetrofitInstance.api.getUsuarioByEmail(correo)

    suspend fun getUsuarios(): List<Usuario>{
        return RetrofitInstance.api.getUsuarios()
    }

    suspend fun getUsuarioById(idUsuario : Int): Usuario {
        return RetrofitInstance.api.getUsuarioById(idUsuario)
    }

}