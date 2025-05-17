package com.invesproject.shared.presentation.viewmodel

import com.invesproject.shared.domain.model.Message
import com.invesproject.shared.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MessageViewModel(
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _state = MutableStateFlow<MessageState>(MessageState.Initial)
    val state: StateFlow<MessageState> = _state.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun sendMessage(senderId: String, receiverId: String, content: String, proposalId: String) {
        scope.launch {
            try {
                _state.value = MessageState.Loading
                val message = Message(
                    id = "",
                    senderId = senderId,
                    receiverId = receiverId,
                    content = content,
                    proposalId = proposalId
                )
                messageRepository.sendMessage(message)
                _state.value = MessageState.Success
            } catch (e: Exception) {
                _state.value = MessageState.Error(e.message ?: "Failed to send message")
            }
        }
    }

    fun getMessagesForProposal(proposalId: String) {
        scope.launch {
            messageRepository.getMessagesForProposal(proposalId)
                .catch { e -> _state.value = MessageState.Error(e.message ?: "Failed to load messages") }
                .collect { messages ->
                    _messages.value = messages
                }
        }
    }

    fun getMessagesForUser(userId: String) {
        scope.launch {
            messageRepository.getMessagesForUser(userId)
                .catch { e -> _state.value = MessageState.Error(e.message ?: "Failed to load messages") }
                .collect { messages ->
                    _messages.value = messages
                }
        }
    }

    fun markMessageAsRead(messageId: String) {
        scope.launch {
            try {
                messageRepository.markMessageAsRead(messageId)
            } catch (e: Exception) {
                _state.value = MessageState.Error(e.message ?: "Failed to mark message as read")
            }
        }
    }
}

sealed class MessageState {
    object Initial : MessageState()
    object Loading : MessageState()
    object Success : MessageState()
    data class Error(val message: String) : MessageState()
} 