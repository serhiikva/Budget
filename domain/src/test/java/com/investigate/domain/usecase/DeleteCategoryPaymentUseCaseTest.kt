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

class DeleteCategoryPaymentUseCaseTest {

    private val repository = mockk<BudgetRepository>(relaxed = true)
    private val updateBudgetUseCase = mockk<UpdateBudgetUseCase>(relaxed = true)
    private val useCase = DeleteCategoryPaymentUseCase(repository, updateBudgetUseCase)

    private val categoryId = "cat-1"
    private val payment = CategoryPayment(id = "pay-1", categoryId = categoryId, amount = 100, note = "lunch", createdDateMillis = 0L)
    private val budget = Budget(
        id = 1,
        creatorEmail = "test@test.com",
        sharedWithEmails = emptyList(),
        startDateMillis = 0L,
        endDateMillis = 0L,
        categories = listOf(
            BudgetCategory(id = categoryId, name = "Food", budgetAmount = 500, payments = listOf(payment))
        ),
        isActive = true,
        lastModified = 0L
    )

    @Test
    fun `skips when budget not found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns null
        useCase(budget.id, payment)
        coVerify(exactly = 0) { updateBudgetUseCase(any()) }
    }

    @Test
    fun `removes the target payment from its category`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        useCase(budget.id, payment)
        coVerify {
            updateBudgetUseCase(withArg { updated ->
                assert(updated.categories.first { it.id == categoryId }.payments.none { it.id == payment.id })
            })
        }
    }

    @Test
    fun `keeps other payments in the same category`() = runTest {
        val other = CategoryPayment(id = "pay-2", categoryId = categoryId, amount = 50, note = "dinner", createdDateMillis = 0L)
        val budgetWithTwo = budget.copy(
            categories = listOf(BudgetCategory(id = categoryId, name = "Food", budgetAmount = 500, payments = listOf(payment, other)))
        )
        coEvery { repository.getBudgetById(budget.id) } returns budgetWithTwo
        useCase(budget.id, payment)
        coVerify {
            updateBudgetUseCase(withArg { updated ->
                assert(updated.categories.first { it.id == categoryId }.payments == listOf(other))
            })
        }
    }
}