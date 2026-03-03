package com.investigate.remotefirebase.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class CategoryPaymentDto(
    val id: String = Uuid.random().toString(),
    val categoryId: String = "",
    val amount: Int = -1,
    val note: String = "",
    val createdDateMillis: Long = -1
)
