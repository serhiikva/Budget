package com.investigate.database.entity

data class BudgetCategoryEntity(
    val id: String,
    val name: String,
    val budgetAmount: Int,
    val spentAmount: Int,
    val payments: List<CategoryPaymentEntity>
)