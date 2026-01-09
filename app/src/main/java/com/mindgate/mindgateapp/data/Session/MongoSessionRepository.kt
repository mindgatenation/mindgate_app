package com.mindgate.mindgateapp.data.Session

import com.mindgate.mindgateapp.data.dao.SessionType
import com.mindgate.mindgateapp.data.dao.Sessions
import com.mindgate.mindgateapp.data.repo.SessionsRepository

class MongoSessionRepository () : SessionsRepository {
    override suspend fun getSessionsList(userEmail: String): List<Sessions> {
        // This is a test list of session the actual list should be fetched feom the
        // database for each user
        return listOf(
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
    }

    override suspend fun addSession(sessions: Sessions): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun initSessionListener(
        email: String,
        onChange: List<Sessions>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getSessionInfo(sessionId: String) {
        TODO("Not yet implemented")
    }

}