package com.mindgate.mindgateapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mindgate.mindgateapp.data.dao.User
import com.mindgate.mindgateapp.data.repo.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Result<User> =
        try {
            val credential =
                GoogleAuthProvider.getCredential(idToken, null)

            val result =
                firebaseAuth.signInWithCredential(credential).await()

            val user = result.user!!

            Result.success(
                User(
                    email = user.email,
                    name = user.displayName,
                    profile_pic = user.photoUrl?.toString()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override fun currentUser(): User? {
        val user = firebaseAuth.currentUser ?: return null
        return User(
            email = user.email,
            name = user.displayName,
            profile_pic = user.photoUrl?.toString()
        )
    }
}

