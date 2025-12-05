package com.ratatin.elitaliano.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object MenuShell : Route("menu_shell")
    data object Carro : Route("carro")
    data object Productos : Route("productos")
    data object Principal : Route("principal")
    data object Pago : Route("pago")
    data object Info : Route("info")

    data object Panel : Route("panel")


}