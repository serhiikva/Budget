package com.investigate.database.entity

data class CategoryPaymentEntity(
    val id: String,
    val categoryId: String,
    val amount: Int,
    val note: String,
    val createdDateMillis: Long
)
