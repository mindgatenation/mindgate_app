package com.mindgate.mindgateapp.data.Session

import android.util.Log
import com.mindgate.mindgateapp.data.dao.SessionType
import com.mindgate.mindgateapp.data.dao.Sessions
import com.mindgate.mindgateapp.data.repo.SessionsRepository

class MongoSessionRepository () : SessionsRepository {
    override suspend fun getSessionsList(userEmail: String): List<Sessions> {
        Log.w("MongoSessionRepository", "THIS IS A TEST REPOSITORY")
        return temp_sessions
    }

    override suspend fun addSession(sessions: Sessions): Boolean {
        Log.w("MongoSessionRepository", "THIS IS A TEST REPOSITORY")
        TODO("Not yet implemented")
        return true
    }

    override suspend fun initSessionListener(
        email: String,
        onChange: List<Sessions>
    ) {
        Log.w("MongoSessionRepository", "THIS IS A TEST REPOSITORY")
        TODO("Not yet implemented")
    }

    override suspend fun getSessionInfo(sessionId: String) {
        Log.w("MongoSessionRepository", "THIS IS A TEST REPOSITORY")

    }

}

private var temp_sessions = mutableListOf(
    Sessions(
        sessionId = "test_1",
        sessionType = SessionType.VIDEO_SESSION,
        professionEmail = "architanant5@gmail.com",
        userEmail = "test.sorea@gmail.com",
        callDuration = 1000L
    ),
    Sessions(
        sessionId = "test_2",
        sessionType = SessionType.AUDIO_SESSION,
        professionEmail = "test.sorea@gmail.com",
        userEmail = "architanant5@gmail.com",
        callDuration = 1000L
    ),
    Sessions(
        sessionId = "test_2",
        sessionType = SessionType.VIDEO_SESSION,
        professionEmail = "test.sorea@gmail.com",
        userEmail = "architanant5@gmail.com",
        callDuration = 1000L
    ),
    Sessions (
        sessionId = "test_3",
        sessionType = SessionType.VIDEO_SESSION,
        professionEmail = "royjit0506@gmail.com",
        userEmail = "architanant5@gmail.com",
        callDuration = 1000L
    )
)