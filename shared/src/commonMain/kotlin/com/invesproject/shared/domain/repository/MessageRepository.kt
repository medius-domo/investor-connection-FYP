package com.invesproject.shared.domain.repository

import com.invesproject.shared.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(message: Message): Message
    fun getMessagesForProposal(proposalId: String): Flow<List<Message>>
    fun getMessagesForUser(userId: String): Flow<List<Message>>
    suspend fun markMessageAsRead(messageId: String)
    suspend fun deleteMessage(messageId: String)
} 