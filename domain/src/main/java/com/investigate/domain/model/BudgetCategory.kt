package com.investigate.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class BudgetCategory(
    val id: String = Uuid.random().toString(),
    val name: String,
    val budgetAmount: Int,
    val payments: List<CategoryPayment>
)