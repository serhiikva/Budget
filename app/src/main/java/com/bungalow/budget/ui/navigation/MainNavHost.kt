package com.bungalow.budget.ui.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bungalow.budget.ui.screen.category_details.CategoryDetailsScreen
import com.bungalow.budget.ui.screen.home.HomeScreen
import com.bungalow.budget.ui.screen.login.LoginScreen
import com.bungalow.budget.ui.screen.budget_settings.StartBudgetScreen
import com.bungalow.budget.ui.screen.history.HistoryScreen

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> {
    null
}

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> {
    null
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    SharedTransitionLayout {
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
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalAnimatedVisibilityScope provides this@composable
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars)
                    ) {
                        HomeScreen(
                            onStartBudgetClick = {
                                navController.navigate(Route.StartBudget.getRoute(-1))
                            },
                            onBudgetSettingsClick = {
                                navController.navigate(Route.StartBudget.getRoute(it))
                            },
                            onCategoryClick = { budgetId, categoryId ->
                                navController.navigate(
                                    Route.CategoryDetails.getRoute(
                                        budgetId,
                                        categoryId
                                    )
                                )
                            },
                            onMenuClick = {
                                navController.navigate(Route.History.route)
                            }
                        )
                    }
                }
            }
            composable(
                Route.StartBudget.route,
                arguments = listOf(navArgument(ARG_BUDGET_ID) { type = NavType.IntType })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    StartBudgetScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
            composable(
                Route.CategoryDetails.route,
                arguments = listOf(
                    navArgument(ARG_BUDGET_ID) { type = NavType.IntType },
                    navArgument(ARG_CATEGORY_ID) { type = NavType.StringType },
                )
            ) {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this@SharedTransitionLayout,
                    LocalAnimatedVisibilityScope provides this@composable
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.systemBars)
                    ) {
                        CategoryDetailsScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
            composable(
                Route.History.route
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    HistoryScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}