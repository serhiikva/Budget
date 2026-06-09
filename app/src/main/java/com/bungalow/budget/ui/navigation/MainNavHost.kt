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
import com.bungalow.budget.ui.screen.payments.PaymentsScreen

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
        NavHost(navController = navController, startDestination = Login) {
            composable<Login> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    LoginScreen(
                        onLoggedIn = {
                            navController.navigate(Home) {
                                popUpTo(Login) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
            composable<Home> {
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
                                navController.navigate(StartBudget(-1))
                            },
                            onBudgetSettingsClick = {
                                navController.navigate(StartBudget(it))
                            },
                            onCategoryClick = { budgetId, categoryId ->
                                navController.navigate(
                                    CategoryDetails(budgetId, categoryId)
                                )
                            },
                            onMenuClick = {
                                navController.navigate(History)
                            },
                            onPaymentsClick = {
                                navController.navigate(Payments)
                            }
                        )
                    }
                }
            }
            composable<StartBudget> {
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
            composable<CategoryDetails> {
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
            composable<History> {
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
            composable<Payments> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) {
                    PaymentsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}