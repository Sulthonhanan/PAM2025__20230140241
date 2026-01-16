package com.example.medicord.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.medicord.UserInterface.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object PasienList : Screen("pasien_list")
    object PasienForm : Screen("pasien_form/{pasienId}") {
        fun createRoute(pasienId: Long = 0) = "pasien_form/$pasienId"
    }
    object PasienDetail : Screen("pasien_detail/{pasienId}") {
        fun createRoute(pasienId: Long) = "pasien_detail/$pasienId"
    }
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Login.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.PasienList.route) {
            PasienListScreen(navController = navController)
        }
        composable(
            route = Screen.PasienForm.route,
            arguments = listOf(
                androidx.navigation.navArgument("pasienId") {
                    type = androidx.navigation.NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val pasienId = backStackEntry.arguments?.getLong("pasienId") ?: 0L
            PasienFormScreen(navController = navController, pasienId = pasienId)
        }
        composable(
            route = Screen.PasienDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("pasienId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val pasienId = backStackEntry.arguments?.getLong("pasienId") ?: 0L
            PasienDetailScreen(navController = navController, pasienId = pasienId)
        }
    }
}
