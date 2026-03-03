package com.investigate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val categories: List<BudgetCategoryEntity>,
    val isActive: Boolean,
    val creatorEmail: String,
    val sharedWithEmails: List<String>,
    val lastModified: Long
)
