package com.mindgate.mindgateapp.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.mindgate.mindgateapp.BuildConfig
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

    @Provides
    @Singleton
    fun getGoogleIdOption () : GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build()
    }

    @Provides
    @Singleton
    fun getGetCredentialRequest (googleIdOption : GetGoogleIdOption) : GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }


}