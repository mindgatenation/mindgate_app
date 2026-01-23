package com.mindgate.mindgateapp.data.repo

import com.mindgate.mindgateapp.data.dao.Professional
import com.mindgate.mindgateapp.data.dao.User

interface UserRepository {
    suspend fun getUserDetails(userId: String): User?
    suspend fun getProfessionalDetails(userId: String): Professional?
}