package com.ratatin.elitaliano.data.producto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Upsert
    suspend fun upsert(producto: ProductoLocal)

    @Query("SELECT * FROM producto ORDER BY idProducto DESC")
    fun getAll(): Flow<List<ProductoLocal>>

    @Query("DELETE FROM producto")
    suspend fun clear()

    @Delete
    suspend fun delete(producto: ProductoLocal)

    @Query("DELETE FROM producto WHERE idProducto = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM producto WHERE idProducto = :id LIMIT 1")
    suspend fun getById(id: Int): ProductoLocal?
}