package com.investigate.remotefirebase.di

import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.investigate.remotefirebase.BuildConfig
import com.investigate.remotefirebase.FirebaseConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteFirebaseModule {

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    fun provideFirebaseConfig(): FirebaseConfig {
        return FirebaseConfig(
            budgetsRef = BuildConfig.FIREBASE_BUDGETS_REF,
            usersRef = BuildConfig.FIREBASE_USERS_REF
        )
    }
}