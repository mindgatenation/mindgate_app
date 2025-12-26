package com.mindgate.mindgateapp.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindgate.mindgateapp.data.dao.AiChat
import  com.mindgate.mindgateapp.data.dao.Pair
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import java.util.UUID

// --- ViewModel ---
@HiltViewModel
class AIChatViewModel @Inject constructor() : ViewModel() {

    // Dummy Data Generation
    private val _history = MutableStateFlow<List<AiChat>>(
        listOf(
            AiChat(
                chat_name = "Pressures of Exam",
                chat_id = "1",
                chats = listOf(
                    Pair(
                        prompt = "I am having issues with my exam, I have issue remembering. I can't remember any points",
                        response = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s."
                    )
                )
            ),
            AiChat(
                chat_name = "Anxiety at Work",
                chat_id = "2",
                chats = listOf(
                    Pair(prompt = "I feel overwhelmed.", response = "Let's break down tasks into smaller steps.")
                )
            )
        )
    )
    val history = _history.asStateFlow()

    // Current selected chat (null means empty "Start" screen)
    private val _currentChat = MutableStateFlow<AiChat?>(null)
    val currentChat = _currentChat.asStateFlow()

    fun selectChat(chatId: String) {
        _currentChat.value = _history.value.find { it.chat_id == chatId }
    }

    fun startNewChat() {
        _currentChat.value = null
    }

    fun sendMessage(prompt: String) {
        val current = _currentChat.value
        if (current == null) {
            // Create new chat
            val newChat = AiChat(
                chat_name = "New Conversation", // In real app, generate based on prompt
                chat_id = UUID.randomUUID().toString(),
                chats = listOf(Pair(prompt, "This is a simulated AI response for: \"$prompt\""))
            )
            _history.update { listOf(newChat) + it }
            _currentChat.value = newChat
        } else {
            // Append to existing
            val updatedPairs = current.chats + Pair(prompt, "This is a simulated AI response for: \"$prompt\"")
            val updatedChat = current.copy(chats = updatedPairs)

            // Update history list
            _history.update { list ->
                list.map { if (it.chat_id == current.chat_id) updatedChat else it }
            }
            _currentChat.value = updatedChat
        }
    }
}