package com.ratatin.elitaliano.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ratatin.elitaliano.data.AppDatabase
import com.ratatin.elitaliano.repository.ProductoRepository

class ProductosViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            val database = AppDatabase.getInstance(context)
            val productoRepo = ProductoRepository(database.productoDao())
            @Suppress("UNCHECKED_CAST")
            return ProductosViewModel(productoRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}