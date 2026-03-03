package com.investigate.database.di


import android.content.Context
import androidx.room.Room
import com.investigate.database.Database
import com.investigate.database.dao.BudgetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "budget.db"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideBudgetDao(db: Database): BudgetDao {
        return db.budgetDao()
    }

}