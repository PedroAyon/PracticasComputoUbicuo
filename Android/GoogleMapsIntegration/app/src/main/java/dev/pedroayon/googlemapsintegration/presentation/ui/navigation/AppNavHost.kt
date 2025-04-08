package dev.pedroayon.googlemapsintegration.presentation.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.pedroayon.googlemapsintegration.presentation.screens.MapScreen
import dev.pedroayon.googlemapsintegration.presentation.screens.StoreListScreen
import dev.pedroayon.googlemapsintegration.presentation.viewmodel.StoreViewModel

@Composable
fun AppNavHost(startDestination: String = "store_list") {
    val navController = rememberNavController()
    val storeViewModel = StoreViewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("store_list") {
            StoreListScreen(
                viewModel = storeViewModel,
                onStoreSelected = { storeId ->
                    navController.navigate("map/$storeId")
                }
            )
        }
        composable(
            route = "map/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getInt("storeId") ?: -1
            MapScreen(
                viewModel = storeViewModel,
                storeId = storeId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}