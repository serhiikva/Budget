package com.investigate.database.entity

data class BudgetCategoryEntity(
    val id: String,
    val name: String,
    val budgetAmount: Int,
    val payments: List<CategoryPaymentEntity>
)