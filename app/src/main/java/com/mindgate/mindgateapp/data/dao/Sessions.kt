package com.mindgate.mindgateapp.data.dao

enum class SessionType {
    VIDEO_SESSION,
    AUDIO_SESSION
}

data class Sessions(
    val sessionId : String,
    val sessionType: SessionType,
    val professionEmail : String,
    val userEmail : String,
    val callDuration: Long
)
