package com.ratatin.elitaliano.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratatin.elitaliano.data.usuario.UsuarioLocal
import com.ratatin.elitaliano.dataSQL.Usuario
import com.ratatin.elitaliano.repository.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val usuarioRepo: UsuarioRepository): ViewModel(){

    private val _usuarioList = MutableLiveData<List<Usuario>>(emptyList())

    val usuarioList : LiveData<List<Usuario>> = _usuarioList

    private val _gettedUser = MutableLiveData<Usuario>()
    val gettedUser: LiveData<Usuario> = _gettedUser

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    init {
        fetchUsuarios()
        viewModelScope.launch {

        }
    }

    private fun fetchUsuarios() {
        viewModelScope.launch {
            try {
                _usuarioList.value = usuarioRepo.getUsuarios()
            } catch (e: Exception){
                println("error al obtener datos: ${e.localizedMessage}")
            }

        }
    }


    fun login(correo: String, contrasena: String, onSuccess: (Usuario) -> Unit) {
        viewModelScope.launch {
            Log.d("Login", "Intento login: $correo / $contrasena")
            val usuario = usuarioRepo.login(correo)
            Log.d("Login", "Usuario encontrado: $usuario")
            if (usuario != null && usuario.password == contrasena) {
                _error.postValue(false)
                onSuccess(usuario)
            } else {
                _error.postValue(true)
            }
        }
    }

    fun getByIdUser(idUsuario: Long?) {
        viewModelScope.launch {
            _gettedUser.value = usuarioRepo.getUsuarioById(idUsuario)
        }
    }
}