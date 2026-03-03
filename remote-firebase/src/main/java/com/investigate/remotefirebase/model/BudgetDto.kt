package com.investigate.remotefirebase.model

data class BudgetDto(
    val id: Int = -1,
    val creatorEmail: String = "",
    val sharedWithEmails: Map<String, Boolean> = emptyMap(),
    val startDateMillis: Long = -1,
    val endDateMillis: Long = -1,
    val categories: Map<String, BudgetCategoryDto> = emptyMap(),
    val active: Boolean = false,
    val lastModified: Long = -1
)
