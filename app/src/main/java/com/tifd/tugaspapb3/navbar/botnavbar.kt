package com.tifd.tugaspapb3.navbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tifd.tugaspapb3.screens.GithubProfileScreen
import com.tifd.tugaspapb3.screens.ListScreen
import com.tifd.tugaspapb3.screens.TugasScreen

@Composable
fun botnavbar(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.ListScreen.route) {
        // Route untuk ListScreen
        composable(Screen.ListScreen.route) {
            ListScreen(navController)
        }

        // Route untuk TugasScreen
        composable(Screen.TugasScreen.route) {
            TugasScreen(navController)
        }

        // Route untuk GithubProfileScreen
        composable(Screen.GithubProfileScreen.route) {
            GithubProfileScreen(
                navController = navController,
                username = "Nahtaff",)
        }
    }
}
