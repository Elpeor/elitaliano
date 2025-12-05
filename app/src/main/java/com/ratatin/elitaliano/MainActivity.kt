package com.ratatin.elitaliano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ratatin.elitaliano.navigation.Route
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ratatin.elitaliano.views.LoginView
import com.ratatin.elitaliano.views.MenuShellView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.Login.route
                ) {

                    composable(Route.Login.route) {
                        LoginView(
                            onStartClick = { userId: Int ->
                                navController.navigate(
                                    "${Route.MenuShell.route}/$userId"
                                )
                            }
                        )
                    }

                    composable(
                        route = "${Route.MenuShell.route}/{userId}",
                        arguments = listOf(
                            navArgument("userId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->

                        val userId: Int =
                            backStackEntry.arguments!!.getInt("userId")

                        MenuShellView(
                            userId = userId,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

