package com.bungalow.budget.ui.model

data class BudgetCategoryUi(
    val id: String,
    val name: String,
    val budgetAmount: Int,
    val spentAmount: Int,
    val payments: List<CategoryPaymentUi>
)
