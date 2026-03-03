package com.investigate.data.mapper

import com.investigate.data.utils.fromBase64
import com.investigate.data.utils.toBase64
import com.investigate.database.entity.BudgetCategoryEntity
import com.investigate.database.entity.BudgetEntity
import com.investigate.database.entity.CategoryPaymentEntity
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment
import com.investigate.remotefirebase.model.BudgetCategoryDto
import com.investigate.remotefirebase.model.BudgetDto
import com.investigate.remotefirebase.model.CategoryPaymentDto

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        categories = this.categories.map { it.toEntity() },
        id = this.id,
        startDateMillis = this.startDateMillis,
        endDateMillis = this.endDateMillis,
        isActive = this.isActive,
        creatorEmail = this.creatorEmail,
        sharedWithEmails = this.sharedWithEmails,
        lastModified = this.lastModified
    )
}

fun BudgetCategory.toEntity(): BudgetCategoryEntity {
    return BudgetCategoryEntity(
        id = this.id,
        name = this.name,
        budgetAmount = this.budgetAmount,
        spentAmount = this.spentAmount,
        payments = this.payments.map { it.toEntity() }
    )
}

fun CategoryPayment.toEntity(): CategoryPaymentEntity {
    return CategoryPaymentEntity(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        note = this.note,
        createdDateMillis = this.createdDateMillis
    )
}

fun BudgetEntity.toModel(): Budget {
    return Budget(
        id = this.id,
        categories = this.categories.map { it.toModel() },
        startDateMillis = this.startDateMillis,
        endDateMillis = this.endDateMillis,
        isActive = this.isActive,
        creatorEmail = this.creatorEmail,
        sharedWithEmails = this.sharedWithEmails,
        lastModified = this.lastModified
    )
}

fun BudgetCategoryEntity.toModel(): BudgetCategory {
    return BudgetCategory(
        id = this.id,
        name = this.name,
        budgetAmount = this.budgetAmount,
        spentAmount = this.spentAmount,
        payments = this.payments.map { it.toModel() }
    )
}

fun CategoryPaymentEntity.toModel(): CategoryPayment{
    return CategoryPayment(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        note = this.note,
        createdDateMillis = this.createdDateMillis
    )
}

fun Budget.toDto(): BudgetDto {
    return BudgetDto(
        id = this.id,
        creatorEmail = this.creatorEmail.toBase64(),
        sharedWithEmails = this.sharedWithEmails.map { it.toBase64() }.associateWith { true },
        startDateMillis = this.startDateMillis,
        endDateMillis = this.endDateMillis,
        categories = this.categories.map { it.toDto() }.associateBy { it.id },
        active = this.isActive,
        lastModified = this.lastModified
    )
}

fun BudgetCategory.toDto(): BudgetCategoryDto {
    return BudgetCategoryDto(
        id = this.id,
        name = this.name,
        budgetAmount = this.budgetAmount,
        spentAmount = this.spentAmount,
        payments = this.payments.map { it.toDto() }.associateBy { it.id }

    )
}

fun CategoryPayment.toDto(): CategoryPaymentDto {
    return CategoryPaymentDto(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        note = this.note,
        createdDateMillis = this.createdDateMillis
    )
}

fun BudgetDto.toModel(): Budget {
    return Budget(
        id = this.id,
        creatorEmail = this.creatorEmail.fromBase64(),
        sharedWithEmails = this.sharedWithEmails.keys.map { it.fromBase64() }.toList(),
        startDateMillis = this.startDateMillis,
        endDateMillis = this.endDateMillis,
        categories = this.categories.values.map { it.toModel() },
        isActive = this.active,
        lastModified = this.lastModified
    )
}

fun BudgetCategoryDto.toModel(): BudgetCategory {
    return BudgetCategory(
        id = this.id,
        name = this.name,
        budgetAmount = this.budgetAmount,
        spentAmount = this.spentAmount,
        payments = this.payments.values.map { it.toModel() }
    )
}

fun CategoryPaymentDto.toModel(): CategoryPayment {
    return CategoryPayment(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        note = this.note,
        createdDateMillis = this.createdDateMillis
    )
}