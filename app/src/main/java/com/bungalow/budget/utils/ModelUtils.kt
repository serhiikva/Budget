package com.bungalow.budget.utils

import com.bungalow.budget.ui.model.BudgetCategoryUi
import com.bungalow.budget.ui.model.BudgetUi
import com.bungalow.budget.ui.model.CategoryPaymentUi
import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment

fun getMockedBudget(): Budget {
    return Budget(
        categories = getMockedCategories(),
        id = 44,
        startDateMillis = System.currentTimeMillis() - (60 * 60 * 1000),
        endDateMillis = 0L,
        isActive = true,
        creatorEmail = "creator@mail.mail",
        sharedWithEmails = listOf("mike@mail.mail", "alex@mail.mail"),
        lastModified = 0
    )
}

fun getMockedCategories(): List<BudgetCategory> {
    return listOf(
        BudgetCategory(
            id = "1",
            "Food",
            5000,
            listOf(CategoryPayment("1", "3",1000, "Payment note", System.currentTimeMillis()))
        ),
        BudgetCategory(
            id = "2",
            "Food",
            5000,
            listOf(CategoryPayment("2", "4",1000, "Payment note", System.currentTimeMillis()))
        )
    )
}

fun Budget.getAmount(): Int {
    return this.categories.sumOf { it.budgetAmount }
}

fun BudgetCategory.getSpentAmount(): Int {
    return this.payments.sumOf { it.amount }
}

fun Budget.getSpentAmount(): Int {
    return this.categories.sumOf { it.getSpentAmount() }
}

fun Budget.toModelUi(): BudgetUi {
    return BudgetUi(
        id = this.id,
        startDateMillis = this.startDateMillis,
        endDateMillis = this.endDateMillis,
        categories = this.categories.map { it.toModelUi() },
        isActive = this.isActive,
        amount = this.getAmount(),
        spentAmount = this.getSpentAmount()
    )
}

fun BudgetCategory.toModelUi(): BudgetCategoryUi {
    return BudgetCategoryUi(
        id = this.id,
        name = this.name.toSentenceCase(),
        budgetAmount = this.budgetAmount,
        spentAmount = this.getSpentAmount(),
        payments = this.payments.map { it.toModelUi() }
    )
}

fun CategoryPayment.toModelUi(): CategoryPaymentUi {
    return CategoryPaymentUi(
        id = this.id,
        amount = this.amount,
        note = this.note,
        createdDateMillis = this.createdDateMillis
    )
}

