package com.mindgate.mindgateapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindgate.mindgateapp.data.dao.Sessions
import com.mindgate.mindgateapp.data.dao.User
import com.mindgate.mindgateapp.data.firebase.FirebaseAuthRepository
import com.mindgate.mindgateapp.data.repo.SessionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionRepo : SessionsRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel(){

    private val _currSessions = MutableStateFlow<List<Sessions>>(emptyList())
    val currSessions = _currSessions.asStateFlow()

    suspend fun setCurrSessions(userEmail : String) {
        _currSessions.value = sessionRepo.getSessionsList(userEmail)
    }

    fun getCurrUser() : User?{
        return firebaseAuthRepository.currentUser()
    }
}