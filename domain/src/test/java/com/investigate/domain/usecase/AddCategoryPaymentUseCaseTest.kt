package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.repository.BudgetRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddCategoryPaymentUseCaseTest {

    private val repository = mockk<BudgetRepository>(relaxed = true)
    private val useCase = AddCategoryPaymentUseCase(repository)

    private val categoryId = "cat-1"
    private val budget = Budget(
        id = 1,
        creatorEmail = "test@test.com",
        sharedWithEmails = emptyList(),
        startDateMillis = 0L,
        endDateMillis = 0L,
        categories = listOf(
            BudgetCategory(id = categoryId, name = "Food", budgetAmount = 500, payments = emptyList())
        ),
        isActive = true,
        lastModified = 0L
    )

    @Test
    fun `skips when amount is zero`() = runTest {
        useCase(budget.id, payment(amount = 0))
        coVerify(exactly = 0) { repository.getBudgetById(any()) }
    }

    @Test
    fun `skips when amount is negative`() = runTest {
        useCase(budget.id, payment(amount = -10))
        coVerify(exactly = 0) { repository.getBudgetById(any()) }
    }

    @Test
    fun `skips update when budget not found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns null
        useCase(budget.id, payment())
        coVerify(exactly = 0) { repository.updateBudget(any()) }
    }

    @Test
    fun `skips update when category not found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        useCase(budget.id, payment(categoryId = "unknown-cat"))
        coVerify(exactly = 0) { repository.updateBudget(any()) }
    }

    @Test
    fun `adds payment to the correct category`() = runTest {
        val p = payment()
        coEvery { repository.getBudgetById(budget.id) } returns budget
        useCase(budget.id, p)
        coVerify {
            repository.updateBudget(withArg { updated ->
                val category = updated.categories.first { it.id == categoryId }
                assert(category.payments.contains(p))
            })
        }
    }

    @Test
    fun `does not modify other categories`() = runTest {
        val otherCategory = BudgetCategory(id = "cat-2", name = "Transport", budgetAmount = 200, payments = emptyList())
        val budgetWithTwo = budget.copy(categories = budget.categories + otherCategory)
        coEvery { repository.getBudgetById(budget.id) } returns budgetWithTwo
        useCase(budget.id, payment())
        coVerify {
            repository.updateBudget(withArg { updated ->
                assert(updated.categories.first { it.id == "cat-2" }.payments.isEmpty())
            })
        }
    }

    private fun payment(categoryId: String = this.categoryId, amount: Int = 100) = CategoryPayment(
        id = "pay-1",
        categoryId = categoryId,
        amount = amount,
        note = "test",
        createdDateMillis = 0L
    )
}