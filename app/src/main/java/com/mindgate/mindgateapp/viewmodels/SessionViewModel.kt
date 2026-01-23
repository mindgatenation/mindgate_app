package com.mindgate.mindgateapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindgate.mindgateapp.data.dao.Professional
import com.mindgate.mindgateapp.data.dao.Sessions
import com.mindgate.mindgateapp.data.dao.User
import com.mindgate.mindgateapp.data.mongo.MongoSessionRepository
import com.mindgate.mindgateapp.data.mongo.MongoUserRepository
import com.mindgate.mindgateapp.data.repo.SessionsRepository
import com.mindgate.mindgateapp.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val mongoSessionRepository: SessionsRepository,
    private val mongoUserRepository: UserRepository
) : ViewModel() {

    private val currentSession = MutableStateFlow<Sessions?>(null)
    private val currProfessional = MutableStateFlow<Professional?>(null)
    private val currUser = MutableStateFlow<User?>(null)

    val session = currentSession.asStateFlow()
    val professional = currProfessional.asStateFlow()
    val user = currUser.asStateFlow()


    fun getProfessional(){
        session.value?.sessionId?.isBlank()?.let {
            if (!it) {
                viewModelScope.launch {
                    currProfessional.value =
                        mongoUserRepository.getProfessionalDetails(session.value!!.sessionId)
                }
            }
        }
    }

    fun getSession(sessionId : String){
        viewModelScope.launch {
            currentSession.value = mongoSessionRepository.getSessionInfo(sessionId)
        }
    }

    fun getUser(userId : String){
        viewModelScope.launch {
            currUser.value = mongoUserRepository.getUserDetails(userId)
        }
    }
}