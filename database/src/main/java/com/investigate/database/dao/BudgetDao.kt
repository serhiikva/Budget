package com.investigate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.investigate.database.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget WHERE id = :id")
    suspend fun getById(id: Int): BudgetEntity?

    @Query("SELECT * FROM budget WHERE id = :id")
    fun getByIdObservable(id: Int): Flow<BudgetEntity?>

    @Query("SELECT * FROM budget WHERE isActive = 1 ORDER BY startDateMillis DESC LIMIT 1")
    suspend fun getActive(): BudgetEntity?

    @Query("SELECT * FROM budget")
    suspend fun getAll(): List<BudgetEntity>

    @Query("SELECT * FROM budget WHERE isActive = 1 ORDER BY startDateMillis DESC LIMIT 1")
    fun getActiveObservable(): Flow<BudgetEntity?>

    @Insert(onConflict = REPLACE)
    suspend fun insert(budget: BudgetEntity)

    @Update(onConflict = REPLACE)
    suspend fun update(budget: BudgetEntity)

    @Upsert
    suspend fun upsert(budget: BudgetEntity)
}