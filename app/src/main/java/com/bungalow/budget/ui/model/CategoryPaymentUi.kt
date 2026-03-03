package com.bungalow.budget.ui.model

data class CategoryPaymentUi(
    val id: String,
    val amount: Int,
    val note: String,
    val createdDateMillis: Long
)
