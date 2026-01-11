package com.mindgate.mindgateapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mindgate.mindgateapp.data.dao.User
import com.mindgate.mindgateapp.data.repo.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
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

    override val currentUser: StateFlow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomainUser())
        }

        firebaseAuth.addAuthStateListener(listener)

        // initial emit
        trySend(firebaseAuth.currentUser?.toDomainUser())

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }.stateIn(
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = firebaseAuth.currentUser?.toDomainUser()
    )
}

fun FirebaseUser.toDomainUser(): User {
    return User(
        email = email,
        name = displayName,
        profile_pic = photoUrl?.toString(),
    )
}

