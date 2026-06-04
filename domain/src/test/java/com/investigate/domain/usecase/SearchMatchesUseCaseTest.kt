package com.investigate.domain.usecase

import com.investigate.domain.model.Budget
import com.investigate.domain.model.BudgetCategory
import com.investigate.domain.model.CategoryPayment
import com.investigate.domain.repository.BudgetRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchMatchesUseCaseTest {

    private val repository = mockk<BudgetRepository>()
    private val useCase = SearchMatchesUseCase(repository)

    private val payment1 = CategoryPayment(id = "p1", categoryId = "cat-1", amount = 120, note = "Lunch at cafe", createdDateMillis = 0L)
    private val payment2 = CategoryPayment(id = "p2", categoryId = "cat-1", amount = 50, note = "Coffee", createdDateMillis = 0L)
    private val payment3 = CategoryPayment(id = "p3", categoryId = "cat-2", amount = 300, note = "Train ticket", createdDateMillis = 0L)

    private val category1 = BudgetCategory(id = "cat-1", name = "Food", budgetAmount = 500, payments = listOf(payment1, payment2))
    private val category2 = BudgetCategory(id = "cat-2", name = "Transport", budgetAmount = 300, payments = listOf(payment3))

    private val budget = Budget(
        id = 1,
        creatorEmail = "test@test.com",
        sharedWithEmails = emptyList(),
        startDateMillis = 0L,
        endDateMillis = 0L,
        categories = listOf(category1, category2),
        isActive = true,
        lastModified = 0L
    )

    @Test
    fun `returns empty list when budget not found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns null
        assertTrue(useCase(budget.id, "lunch").isEmpty())
    }

    @Test
    fun `returns only payments matching the search term`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        val result = useCase(budget.id, "lunch")
        assertEquals(1, result.size)
        assertEquals("cat-1", result[0].id)
        assertEquals(listOf(payment1), result[0].payments)
    }

    @Test
    fun `search is case insensitive`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        val result = useCase(budget.id, "LUNCH")
        assertEquals(1, result.size)
        assertEquals(payment1.id, result[0].payments[0].id)
    }

    @Test
    fun `matches category by name and returns it with empty payments`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        val result = useCase(budget.id, "Food")
        assertEquals(1, result.size)
        assertEquals("cat-1", result[0].id)
        assertTrue(result[0].payments.isEmpty())
    }

    @Test
    fun `payment match takes priority over category name match`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        val result = useCase(budget.id, "coffee")
        assertEquals(1, result.size)
        assertEquals(listOf(payment2), result[0].payments)
    }

    @Test
    fun `matches payment by amount`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        val result = useCase(budget.id, "300")
        assertEquals(1, result.size)
        assertEquals("cat-2", result[0].id)
    }

    @Test
    fun `returns nothing when no matches found`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        assertTrue(useCase(budget.id, "xyz123").isEmpty())
    }

    @Test
    fun `returns matches across multiple categories`() = runTest {
        coEvery { repository.getBudgetById(budget.id) } returns budget
        val result = useCase(budget.id, "t")
        assertEquals(2, result.size)
    }
}
