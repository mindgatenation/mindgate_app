package com.mindgate.mindgateapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindgate.mindgateapp.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signInWithGoogle(idToken: String) =
        viewModelScope.launch {
        val result = authRepository.signInWithGoogle(idToken)
        // handle success / error
        }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }

    fun currentUser() = authRepository.currentUser()
}