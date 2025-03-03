package com.sobolevkir.appexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sobolevkir.appexplorer.presentation.screen.app_details.AppDetailsScreen
import com.sobolevkir.appexplorer.presentation.screen.installed_apps.InstalledAppsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.InstalledAppsRoute,
        modifier = modifier
    ) {

        val navigateAction: (Route) -> Unit = { navigateTo ->
            when (navigateTo) {
                is Route.BackRoute -> navController.navigateUp()
                else -> navController.navigate(navigateTo)
            }
        }

        composable<Route.InstalledAppsRoute> {
            InstalledAppsScreen(onNavigateTo = navigateAction)
        }

        composable<Route.AppDetailsRoute> {
            AppDetailsScreen(onNavigateTo = navigateAction)
        }

    }
}