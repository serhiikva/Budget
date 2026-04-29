package com.bungalow.budget.ui.model

data class PaymentItemUi(
    val id: String,
    val categoryName: String,
    val amount: Int,
    val note: String,
    val createdDateMillis: Long
)
