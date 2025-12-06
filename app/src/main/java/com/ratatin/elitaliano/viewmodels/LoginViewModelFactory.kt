package com.ratatin.elitaliano.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ratatin.elitaliano.data.AppDatabase
import com.ratatin.elitaliano.repository.UsuarioRepository

class LoginViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val usuarioRepo = UsuarioRepository()
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(usuarioRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}