package com.mindgate.mindgateapp.di

import com.mindgate.mindgateapp.data.mongo.MongoSessionRepository
import com.mindgate.mindgateapp.data.mongo.MongoUserRepository
import com.mindgate.mindgateapp.data.repo.SessionsRepository
import com.mindgate.mindgateapp.data.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MongoModule {

    @Provides
    @Singleton
    fun getSessionsRepository () : SessionsRepository {
        return MongoSessionRepository()
    }

    @Provides
    @Singleton
    fun getUserRepository () : UserRepository {
        return MongoUserRepository()
    }
}