

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
import com.example.proyecto.feature_perfil.PerfilScreen
import com.example.proyecto.feature_admin.AdminUsuariosScreen
import com.example.proyecto.data.SessionManager
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.proyecto.feature_admin.AdminMesasScreen
import com.example.proyecto.feature_admin.AdminRegistrarUsuarioScreen
import com.example.proyecto.feature_admin.AdminUsuarioDetalleScreen
import com.example.proyecto.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
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
                onPerfil = {
                    navController.navigate(NavRoutes.PERFIL)
                },
                onCerrarSesion = {
                    SessionManager.logout()
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                onTerminos = {
                    // Por ahora no hacemos nada, o podrías ir a otra pantalla
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
                onProductoSeleccionado = { /* opcional */ },
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
        composable(NavRoutes.ADMIN_MESAS) {
            AdminMesasScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.PERFIL) {
            PerfilScreen(
                onBack = { navController.popBackStack() },
                onAdminUsuarios = {
                    navController.navigate(NavRoutes.ADMIN_USERS)
                },
                onRegistrarUsuario = {
                    navController.navigate(NavRoutes.ADMIN_REGISTRAR_USUARIO)
                },
                onAdminMesas = {
                    navController.navigate(NavRoutes.ADMIN_MESAS)
                },
                onCerrarSesionTodos = {
                    SessionManager.logoutAllDevices()
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.ADMIN_REGISTRAR_USUARIO) {
            AdminRegistrarUsuarioScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.ADMIN_USERS) {
            AdminUsuariosScreen(
                onBack = { navController.popBackStack() },
                onUserClick = { username ->
                    navController.navigate(NavRoutes.adminUsuarioDetalle(username))
                }
            )
        }
        composable(
            route = NavRoutes.ADMIN_USER_DETAIL,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            AdminUsuarioDetalleScreen(
                username = username,
                onBack = { navController.popBackStack() }
            )
        }




        composable(NavRoutes.ADMIN_USERS) {
            AdminUsuariosScreen(
                onBack = { navController.popBackStack() },
                onUserClick = { username ->
                    navController.navigate(NavRoutes.adminUsuarioDetalle(username))
                }
            )
        }
    }
}
