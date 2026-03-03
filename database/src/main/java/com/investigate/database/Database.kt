package com.investigate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.investigate.database.converter.BudgetConverter
import com.investigate.database.dao.BudgetDao
import com.investigate.database.entity.BudgetEntity

@Database(
    entities = [
        BudgetEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BudgetConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
}