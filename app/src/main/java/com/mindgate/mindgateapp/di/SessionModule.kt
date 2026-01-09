package com.mindgate.mindgateapp.di

import com.mindgate.mindgateapp.data.Session.MongoSessionRepository
import com.mindgate.mindgateapp.data.repo.SessionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun getSessionsRepository () : SessionsRepository {
        return MongoSessionRepository()
    }
}