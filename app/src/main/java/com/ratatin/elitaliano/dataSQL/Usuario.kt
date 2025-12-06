package com.ratatin.elitaliano.dataSQL

data class Usuario(
    val idUsuario: Long? = null,
    val nombre: String,
    val email: String,
    val password: String,
    val admin: Boolean
)