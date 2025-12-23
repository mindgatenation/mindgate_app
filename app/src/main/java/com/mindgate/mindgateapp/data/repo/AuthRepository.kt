package com.mindgate.mindgateapp.data.repo

import com.mindgate.mindgateapp.data.dao.User

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<User>

    suspend fun signOut()

    fun currentUser(): User?
}