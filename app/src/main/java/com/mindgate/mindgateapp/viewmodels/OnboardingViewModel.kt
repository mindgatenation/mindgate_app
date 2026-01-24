package com.mindgate.mindgateapp.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getCredentialRequest : GetCredentialRequest,
) : ViewModel() {
    private val _userEmail : MutableStateFlow<String?> = MutableStateFlow(null)
    val userEmail = _userEmail.asStateFlow()
    val request = getCredentialRequest

    val currentUser = authRepository.currentUser

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun signInWithGoogle(activity: Activity) {
        val credentialManager = CredentialManager.create(activity)

        viewModelScope.launch {
            try {
                // SMALL delay fixes first-launch race
                delay(300)

                val result = credentialManager.getCredential(
                    context = activity,
                    request = request
                )

                val credential = result.credential
                if (
                    credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCred =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val signInResult =
                        authRepository.signInWithGoogle(googleCred.idToken)

                    if (signInResult.isSuccess) {
                        _userEmail.value =
                            authRepository.currentUser.value?.email
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value =
                            LoginState.Error(signInResult.exceptionOrNull()?.message ?: "Auth failed")
                    }
                }

            } catch (e: NoCredentialException) {
                // THIS IS NORMAL on first launch
                Log.d("CredExp", "No credential yet (expected)")
                _loginState.value = LoginState.Error("No creds")
            } catch (e: GetCredentialCancellationException) {
                Log.d("CredExp", "User cancelled")
            } catch (e: Exception) {
                Log.e("CredExp", "Unexpected error", e)
                _loginState.value = LoginState.Error("Unexpected error")
            }
        }
    }


}