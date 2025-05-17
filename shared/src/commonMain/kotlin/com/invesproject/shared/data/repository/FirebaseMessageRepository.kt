package com.invesproject.shared.data.repository

import com.invesproject.shared.domain.model.Message
import com.invesproject.shared.domain.repository.MessageRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseMessageRepository : MessageRepository {
    private val firestore = Firebase.firestore
    private val messagesCollection = firestore.collection("messages")

    override suspend fun sendMessage(message: Message): Message {
        val docRef = messagesCollection.document()
        val messageWithId = message.copy(id = docRef.id)
        docRef.set(messageWithId)
        return messageWithId
    }

    override fun getMessagesForProposal(proposalId: String): Flow<List<Message>> {
        return messagesCollection
            .where("proposalId", "==", proposalId)
            .orderBy("timestamp", "asc")
            .snapshots
            .map { snapshot -> snapshot.documents.mapNotNull { it.data() } }
    }

    override fun getMessagesForUser(userId: String): Flow<List<Message>> {
        return messagesCollection
            .where("receiverId", "==", userId)
            .orderBy("timestamp", "desc")
            .snapshots
            .map { snapshot -> snapshot.documents.mapNotNull { it.data() } }
    }

    override suspend fun markMessageAsRead(messageId: String) {
        messagesCollection.document(messageId).update("isRead", true)
    }

    override suspend fun deleteMessage(messageId: String) {
        messagesCollection.document(messageId).delete()
    }
} 