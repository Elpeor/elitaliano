package com.ratatin.elitaliano.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ratatin.elitaliano.repository.UsuarioRepository
import kotlinx.coroutines.launch

class RegistroViewModel(private val context: Context) : ViewModel() {

    private val repo = UsuarioRepository()

    private val _registroExitoso = MutableLiveData<Long?>()
    val registroExitoso: LiveData<Long?> get() = _registroExitoso

    private val _error = MutableLiveData("")
    val error: LiveData<String> get() = _error

    fun registrarUsuario(nombre: String, email: String, password: String) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            _error.value = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch {
            try {
                val id = repo.createUsuario(
                        nombre = nombre,
                        email = email,
                        password = password,
                        admin = false

                )
                _registroExitoso.postValue(id.idUsuario)

            } catch (e: Exception) {
                _error.postValue("Error al registrar usuario")
            }
        }
    }
}

class RegistroViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistroViewModel(context) as T
    }
}