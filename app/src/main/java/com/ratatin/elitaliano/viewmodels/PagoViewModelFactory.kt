package com.ratatin.elitaliano.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ratatin.elitaliano.data.AppDatabase
import com.ratatin.elitaliano.repository.PagoRepository

class PagoViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagoViewModel::class.java)) {
            val database = AppDatabase.getInstance(context)
            val pagoRepo = PagoRepository()
            @Suppress("UNCHECKED_CAST")
            return PagoViewModel(pagoRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}