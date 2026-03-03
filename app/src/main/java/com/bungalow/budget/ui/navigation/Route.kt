package com.bungalow.budget.ui.navigation

const val ROUTE_LOGIN = "login"
const val ROUTE_HOME = "home"
const val ROUTE_START_BUDGET = "startBudget"
const val ARG_BUDGET_ID = "budgetId"

sealed class Route(val route: String) {
    object Login : Route(ROUTE_LOGIN)

    object Home : Route(ROUTE_HOME)

    object StartBudget : Route("$ROUTE_START_BUDGET/{$ARG_BUDGET_ID}") {
        fun getRoute(budgetId: Int?): String {
            return budgetId?.let {
                "$ROUTE_START_BUDGET/$budgetId"
            } ?: route
        }
    }
}