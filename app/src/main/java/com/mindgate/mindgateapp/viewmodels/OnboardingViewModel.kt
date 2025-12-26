package com.mindgate.mindgateapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindgate.mindgateapp.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val context : Application
) : ViewModel() {
    private val _userEmail : MutableStateFlow<String?> = MutableStateFlow(null)
    val userEmail = _userEmail.asStateFlow()

    fun signInWithGoogle(idToken: String) =
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isSuccess){
                Toast.makeText(context,"Credentials Found", Toast.LENGTH_SHORT).show()
                _userEmail.value = authRepository.currentUser()?.email
            }
            else{
                Toast.makeText(context,"No Credentials Found", Toast.LENGTH_SHORT).show()
            }
        }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }

    fun currentUser() = authRepository.currentUser()
}