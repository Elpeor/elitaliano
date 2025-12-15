package com.ratatin.elitaliano.views

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.ratatin.elitaliano.navigation.Route
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ratatin.elitaliano.viewmodels.LoginViewModel
import com.ratatin.elitaliano.viewmodels.LoginViewModelFactory
import com.ratatin.elitaliano.viewmodels.ProductosViewModel
import com.ratatin.elitaliano.viewmodels.ProductosViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShellView(
    userId: Long?,
    navController: NavHostController,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val innerNavController = rememberNavController()
    val context = LocalContext.current

    val productosViewModel: ProductosViewModel = viewModel(
        factory = ProductosViewModelFactory(context)
    )
    val loginViewModel : LoginViewModel = viewModel(
        factory = LoginViewModelFactory()
    )
    LaunchedEffect(userId) {
        loginViewModel.getByIdUser(userId)
    }
    val usuario by loginViewModel.gettedUser.observeAsState()



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Carrito") },
                    selected = currentInnerRoute(innerNavController) == Route.Carro.route,
                    onClick = {
                        innerNavController.navigate(Route.Carro.route) {
                            popUpTo(Route.Principal.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Productos") },
                    selected = currentInnerRoute(innerNavController) == Route.Productos.route,
                    onClick = {
                        innerNavController.navigate(Route.Productos.route) {
                            popUpTo(Route.Principal.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Principal") },
                    selected = currentInnerRoute(innerNavController) == Route.Principal.route,
                    onClick = {
                        innerNavController.navigate(Route.Principal.route) {
                            popUpTo(Route.Principal.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Pagos") },
                    selected = currentInnerRoute(innerNavController) == Route.Pago.route,
                    onClick = {
                        innerNavController.navigate(Route.Pago.route) {
                            popUpTo(Route.Principal.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Quienes somos") },
                    selected = currentInnerRoute(innerNavController) == Route.Info.route,
                    onClick = {
                        innerNavController.navigate(Route.Info.route) {
                            popUpTo(Route.Principal.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                if (usuario?.admin ?: false) {
                    NavigationDrawerItem(
                        label = { Text("Panel") },
                        selected = currentInnerRoute(innerNavController) == Route.Panel.route,
                        onClick = {
                            innerNavController.navigate(Route.Panel.route) {
                                popUpTo(Route.Principal.route) { inclusive = false }
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("El Italiano") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            navController.navigate(Route.Login.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }) {
                            Text(
                                text = "Cerrar sesión",
                                color = Color.Blue
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
                startDestination = Route.Principal.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Route.Carro.route) { CarroView(productosViewModel, usuario ) }
                composable(Route.Productos.route) { ProductosView(navController = innerNavController, productosViewModel) }
                composable(Route.Principal.route) { PrincipalView() }
                composable ( Route.Pago.route ) {PagoView(userId) }
                composable(Route.Info.route) { InfoView() }
                composable(Route.Panel.route) { PanelView(navController = innerNavController) }
                composable(Route.ProductosPanel.route) { ProductosPanelView(productosViewModel) }
            }
        }
    }
}

@Composable
private fun currentInnerRoute(navController: NavHostController): String? {
    val entry by navController.currentBackStackEntryAsState()
    return entry?.destination?.route
}