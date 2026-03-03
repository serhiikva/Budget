package com.investigate.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class CategoryPayment(
    val id: String = Uuid.random().toString(),
    val categoryId: String,
    val amount: Int,
    val note: String,
    val createdDateMillis: Long
)
