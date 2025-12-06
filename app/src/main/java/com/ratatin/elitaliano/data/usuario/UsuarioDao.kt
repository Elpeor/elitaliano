package com.ratatin.elitaliano.data.usuario

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface UsuarioDao {
    @Upsert
    suspend fun upsert(usuarioLocal: UsuarioLocal)

    @Query("SELECT * FROM usuario ORDER BY idUsuario DESC")
    fun getAll(): Flow<List<UsuarioLocal>>

    @Query("DELETE FROM usuario")
    suspend fun clear()

    @Query("SELECT * FROM usuario WHERE email = :correo LIMIT 1")
    suspend fun getByCorreo(correo: String): UsuarioLocal?

    @Query("SELECT * FROM usuario WHERE email = :correo AND password = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): UsuarioLocal?

}