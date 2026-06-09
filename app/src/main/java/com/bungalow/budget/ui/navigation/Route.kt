package com.bungalow.budget.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
object Login : Route

@Serializable
object Home : Route

@Serializable
data class StartBudget(
    val budgetId: Int? = null
) : Route

@Serializable
data class CategoryDetails(
    val budgetId: Int,
    val categoryId: String
) : Route

@Serializable
object History : Route

@Serializable
object Payments : Route