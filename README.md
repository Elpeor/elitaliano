Archivo README.md con:
o Nombre de la app: italiano.
o Nombres de los integrantes: Jose Torreblanca, Vicente Colina.
o Funcionalidades: las funcionalidades que contiene nuestra aplicacion parten de lo mas basico hasta lo mas importante en una aplicacion de restuarante en este caso su respectivo carrito de compra, el agregar mas productos o eliminarlos de su carrito de compra, y en el caso de administrador tiene todas sus correspondencias como el poder eliminar y agregar productos.
o Endpoints usados (propios y externos): todos los endpoint:@GET("/api/v1/productos")
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
    suspend fun createPagos(@Body pago: Pago): Pago.
o Instrucciones para ejecutar el proyecto:puede la persona registrarse y entrar con su usuario ya registrado o el iniciar con un usuario admin que ya estan creados con anterioridad.
o APK firmado y ubicación del archivo .jks: no solicitado.
o Código fuente de microservicios y app móvil:https://github.com/Elpeor/elitaliano.
o Evidencia de trabajo colaborativo (commits por persona): avance en discord. 
