package com.ratatin.elitaliano.remote
import com.ratatin.elitaliano.data.usuario.UsuarioLocal
import com.ratatin.elitaliano.dataSQL.Pago
import com.ratatin.elitaliano.dataSQL.Producto
import com.ratatin.elitaliano.dataSQL.Usuario
import retrofit2.http.*
import retrofit2.http.GET


interface ApiService {

    @GET("/api/v1/productos")
    suspend fun getProductos(): List<Producto>

    @POST("/api/v1/productos/crear")
    suspend fun createProductos(@Body producto: Producto) : Producto

    @PUT("/api/v1/productos/actualizar/{id}")
    suspend fun updateProducto(@Path("id", encoded = true) id : Long?, @Body producto: Producto)

    @DELETE("/api/v1/productos/eliminar/{id}")
    suspend fun deleteProductoPorId(@Path("id", encoded = true) id : Long?)

    @GET("/api/v1/usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @GET("/api/v1/usuarios/email/{email}")
    suspend fun getUsuarioByEmail(@Path("email", encoded = true) email: String): Usuario

    @GET("/api/v1/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id", encoded = true) idUsuario: Long?): Usuario

    @POST("/api/v1/usuarios/crear")
    suspend fun createUsuarios(@Body usuario: Usuario) : Usuario

    @PUT("/api/v1/usuarios/{id}")
    suspend fun updateUsuario(@Path("id", encoded = true) id : Long?, @Body usuario: Usuario)

    @DELETE("/api/v1/usuarios/eliminar/{id}")
    suspend fun deleteUsuarioPorId(@Path("id", encoded = true) id : Long?)

    @GET("/api/v1/pagos")
    suspend fun getPagos(): List<Pago>

    @GET("/api/v1/pagos/user/{id}")
    suspend fun getPagosByIdUsuario(@Path("id", encoded = true) idUsuario: Long?): List<Pago>

    @POST("/api/v1/pagos/crear")
    suspend fun createPagos(@Body pago: Pago): Pago
}


