package com.example.proyecto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto.feature_mesas.MesasScreen
import com.example.proyecto.feature_menu.MenuScreen
import com.example.proyecto.feature_orden.OrdenScreen
import com.example.proyecto.feature_login.LoginScreen
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.MESAS) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.MESAS) {
            MesasScreen(
                onMesaSelected = { mesaId ->
                    navController.navigate(NavRoutes.orden(mesaId))
                },
                onIrAlMenu = {
                    navController.navigate(NavRoutes.menu(0))
                }
            )
        }

        composable(
            route = NavRoutes.MENU,
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            MenuScreen(
                mesaId = mesaId,
                onProductoSeleccionado = { /* agregar en repo y regresar */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.ORDEN,
            arguments = listOf(
                navArgument("mesaId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
            OrdenScreen(
                mesaId = mesaId,
                onBack = { navController.popBackStack() },
                onAgregarProducto = {
                    navController.navigate(NavRoutes.menu(mesaId))
                }
            )
        }
    }
}