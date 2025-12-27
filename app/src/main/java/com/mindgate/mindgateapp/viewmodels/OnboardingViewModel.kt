package com.mindgate.mindgateapp.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.mindgate.mindgateapp.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getCredentialRequest : GetCredentialRequest
) : ViewModel() {
    private val _userEmail : MutableStateFlow<String?> = MutableStateFlow(null)
    val userEmail = _userEmail.asStateFlow()
    val request = getCredentialRequest

    fun signInWithGoogle(context: Context) {
        val credentialManager = CredentialManager.create(context)
        viewModelScope.launch {
            runCatching {
                credentialManager.getCredential(
                    context = context,
                    request = request
                )
            }.onSuccess { result ->
                val credential = result.credential

                if (
                    credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCred =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val result = authRepository.signInWithGoogle(googleCred.idToken)
                    if (result.isSuccess) {
                        Toast.makeText(context, "Credentials Found", Toast.LENGTH_SHORT).show()
                        _userEmail.value = authRepository.currentUser()?.email
                    } else {
                        Toast.makeText(context, "No Credentials Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }.onFailure { throwable ->
                when (throwable) {
                    is NoCredentialException -> {
                        Log.d("CredExp", "No credential available (expected)")
                        Toast.makeText(
                            context,
                            "No credential available. Please login with Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is GetCredentialCancellationException -> {
                        Log.d("CredExp", "User cancelled")
                    }

                    else -> {
                        Log.e("CredExp", "Unexpected error", throwable)
                    }
                }
            }

        }
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }

    fun currentUser() = authRepository.currentUser()
}