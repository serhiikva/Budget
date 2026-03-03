package com.investigate.data.di

import com.google.firebase.auth.FirebaseAuth
import com.investigate.data.AuthRepositoryImpl
import com.investigate.data.BudgetRepositoryImpl
import com.investigate.data.RemoteRepositoryImpl
import com.investigate.database.dao.BudgetDao
import com.investigate.domain.repository.AuthRepository
import com.investigate.domain.repository.BudgetRepository
import com.investigate.domain.repository.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    abstract fun bindRemoteRepository(
        impl: RemoteRepositoryImpl
    ): RemoteRepository

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}