package com.mindgate.mindgateapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindgate.mindgateapp.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val context : Application
) : ViewModel() {
    fun signInWithGoogle(idToken: String) =
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isSuccess){
                Toast.makeText(context,"Login Successful", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }

    fun currentUser() = authRepository.currentUser()
}