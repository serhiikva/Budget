package com.bungalow.budget.ui.model

data class BudgetUi(
    val id: Int = 0,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val categories: List<BudgetCategoryUi>,
    val isActive: Boolean,
    val amount: Int,
    val spentAmount: Int,
)
