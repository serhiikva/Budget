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

class UpdateCategoryPaymentUseCaseTest {

    private val repository = mockk<BudgetRepository>(relaxed = true)
    private val updateBudgetUseCase = mockk<UpdateBudgetUseCase>(relaxed = true)
    private val useCase = UpdateCategoryPaymentUseCase(repository, updateBudgetUseCase)

    private val categoryId = "cat-1"
    private val original = CategoryPayment(id = "pay-1", categoryId = categoryId, amount = 100, note = "lunch", createdDateMillis = 0L)
    private val budget = Budget(
        id = 1,
        creatorEmail = "test@test.com",
        sharedWithEmails = emptyList(),
        startDateMillis = 0L,
        endDateMillis = 0L,
        categories = listOf(
            BudgetCategory(id = categoryId, name = "Food", budgetAmount = 500, payments = listOf(original))
        ),
        isActive = true,
        lastModified = 0L
    )

    @Test
    fun `skips when budget not found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns null
        useCase(budget.id, original.copy(amount = 200))
        coVerify(exactly = 0) { updateBudgetUseCase(any()) }
    }

    @Test
    fun `replaces payment with updated version`() = runTest {
        val updated = original.copy(amount = 250, note = "updated lunch")
        coEvery { repository.getBudgetById(budget.id) } returns budget
        useCase(budget.id, updated)
        coVerify {
            updateBudgetUseCase(withArg { result ->
                assert(result.categories.first { it.id == categoryId }.payments == listOf(updated))
            })
        }
    }

    @Test
    fun `does not alter unrelated categories`() = runTest {
        val otherCat = BudgetCategory(id = "cat-2", name = "Transport", budgetAmount = 200, payments = emptyList())
        coEvery { repository.getBudgetById(budget.id) } returns budget.copy(categories = budget.categories + otherCat)
        useCase(budget.id, original.copy(amount = 250))
        coVerify {
            updateBudgetUseCase(withArg { result ->
                assert(result.categories.first { it.id == "cat-2" }.payments.isEmpty())
            })
        }
    }
}