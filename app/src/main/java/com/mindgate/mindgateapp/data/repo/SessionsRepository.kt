package com.mindgate.mindgateapp.data.repo

import com.mindgate.mindgateapp.data.dao.Sessions

interface SessionsRepository {

    suspend fun getSessionsList(userEmail : String) : List<Sessions>

    suspend fun addSession(sessions: Sessions) : Boolean

    suspend fun initSessionListener(email : String, onChange : (List<Sessions>))

    suspend fun getSessionInfo(sessionId : String): Sessions?
}