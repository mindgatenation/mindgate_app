package com.mindgate.mindgateapp.di

import com.google.firebase.auth.FirebaseAuth
import com.mindgate.mindgateapp.data.firebase.FirebaseAuthRepository
import com.mindgate.mindgateapp.data.repo.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository =
        FirebaseAuthRepository(firebaseAuth)
}