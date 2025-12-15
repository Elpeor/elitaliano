package com.ratatin.elitaliano.repository

import com.ratatin.elitaliano.data.usuario.UsuarioLocal
import com.ratatin.elitaliano.dataSQL.Producto
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

    suspend fun actualizar(nombre:String, email:String, password: String, admin: Boolean, id : Long?){
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(password.isNotBlank()){"La contraseña no puede estar vacía"}
        RetrofitInstance.api.updateUsuario(id, Usuario(nombre = nombre.trim(), email = email.trim(), password = password, admin = admin))
    }

    suspend fun eliminarPorId(id: Long?) {
        RetrofitInstance.api.deleteUsuarioPorId(id)
    }
}