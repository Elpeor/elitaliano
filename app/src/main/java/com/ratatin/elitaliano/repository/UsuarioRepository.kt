package com.ratatin.elitaliano.repository

import com.ratatin.elitaliano.data.usuario.UsuarioLocal
import com.ratatin.elitaliano.dataSQL.Usuario
import com.ratatin.elitaliano.remote.RetrofitInstance

class UsuarioRepository(){

    suspend fun login(correo: String) = RetrofitInstance.api.getUsuarioByEmail(correo)

    suspend fun getUsuarios(): List<Usuario>{
        return RetrofitInstance.api.getUsuarios()
    }

    suspend fun getUsuarioById(idUsuario : Long?): Usuario {
        return RetrofitInstance.api.getUsuarioById(idUsuario)
    }

    suspend fun createUsuario(nombre : String, email : String, password : String, admin : Boolean): Usuario {
        return RetrofitInstance.api.createUsuarios(Usuario(nombre = nombre, email = email, password = password, admin = admin))
    }
}