package com.investigate.remotefirebase.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class BudgetCategoryDto(
    val id: String = Uuid.random().toString(),
    val name: String = "",
    val budgetAmount: Int = -1,
    val spentAmount: Int = -1,
    val payments: Map<String, CategoryPaymentDto> = emptyMap()
)