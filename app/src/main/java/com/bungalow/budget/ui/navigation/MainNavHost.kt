package com.bungalow.budget.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bungalow.budget.ui.screen.home.HomeScreen
import com.bungalow.budget.ui.screen.login.LoginScreen
import com.bungalow.budget.ui.screen.start_budget.StartBudgetScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Login.route) {
        composable(Route.Login.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                LoginScreen(
                    onLoggedIn = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        composable(Route.Home.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                HomeScreen(
                    onStartBudgetClick = { navController.navigate(Route.StartBudget.getRoute(-1)) },
                    onBudgetSettingsClick = { navController.navigate(Route.StartBudget.getRoute(it)) }
                )
            }
        }
        composable(
            Route.StartBudget.route,
            arguments = listOf(navArgument(NavArg.BUDGET_ID) { type = NavType.IntType })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                StartBudgetScreen()
            }
        }
    }
}