package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.repository.BudgetRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteCategoryUseCaseTest {

    private val repository = mockk<BudgetRepository>(relaxed = true)
    private val updateBudgetUseCase = mockk<UpdateBudgetUseCase>(relaxed = true)
    private val useCase = DeleteCategoryUseCase(repository, updateBudgetUseCase)

    private val budget = Budget(
        id = 1,
        creatorEmail = "test@test.com",
        sharedWithEmails = emptyList(),
        startDateMillis = 0L,
        endDateMillis = 0L,
        categories = listOf(
            BudgetCategory(id = "cat-1", name = "Food", budgetAmount = 500, payments = emptyList()),
            BudgetCategory(id = "cat-2", name = "Transport", budgetAmount = 200, payments = emptyList())
        ),
        isActive = true,
        lastModified = 0L
    )

    @Test
    fun `skips when budget not found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns null
        useCase(budget.id, "cat-1")
        coVerify(exactly = 0) { updateBudgetUseCase(any()) }
    }

    @Test
    fun `removes the target category`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        useCase(budget.id, "cat-1")
        coVerify {
            updateBudgetUseCase(withArg { updated ->
                assert(updated.categories.none { it.id == "cat-1" })
            })
        }
    }

    @Test
    fun `keeps other categories intact`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        useCase(budget.id, "cat-1")
        coVerify {
            updateBudgetUseCase(withArg { updated ->
                assert(updated.categories.any { it.id == "cat-2" })
            })
        }
    }
}
