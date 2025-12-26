package com.mindgate.mindgateapp.data.dao

data class AiChat(
    val chat_name: String,
    val chat_id : String,
    val chats : List<Pair>
)

data class Pair(
    val prompt : String,
    val response : String
)