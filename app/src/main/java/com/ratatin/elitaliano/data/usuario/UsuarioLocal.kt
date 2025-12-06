package com.ratatin.elitaliano.data.usuario

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioLocal(
    @PrimaryKey(autoGenerate = true) val idUsuario: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val admin: Boolean
)