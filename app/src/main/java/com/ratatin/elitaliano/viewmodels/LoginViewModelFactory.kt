package com.ratatin.elitaliano.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ratatin.elitaliano.data.AppDatabase
import com.ratatin.elitaliano.repository.UsuarioRepository

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val database = AppDatabase.getInstance(context)
            val usuarioRepo = UsuarioRepository(database.usuarioDao())
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(usuarioRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}