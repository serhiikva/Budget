package com.investigate.domain.model

data class Budget(
    val id: Int = 0,
    val creatorEmail: String,
    val sharedWithEmails: List<String>,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val categories: List<BudgetCategory>,
    val isActive: Boolean,
    val lastModified: Long
) {

    companion object {
        fun empty(): Budget {
            return Budget(
                id = 0,
                startDateMillis = 0L,
                endDateMillis = 0L,
                categories = emptyList(),
                isActive = true,
                creatorEmail = "",
                sharedWithEmails = emptyList(),
                lastModified = 0
            )
        }
    }
}
