package com.invesproject.shared.data.repository

import com.invesproject.shared.domain.model.Message
import com.invesproject.shared.domain.repository.MessageRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import dev.gitlive.firebase.firestore.orderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseMessageRepository : MessageRepository {
    private val firestore = Firebase.firestore
    private val messagesCollection = firestore.collection("messages")

    override suspend fun sendMessage(message: Message): Message {
        val docRef = messagesCollection.document("")
        val messageWithId = message.copy(id = docRef.id)
        docRef.set(messageWithId)
        return messageWithId
    }

    override fun getMessagesForProposal(proposalId: String): Flow<List<Message>> {
        return messagesCollection
            .where("proposalId", "==", proposalId)
            .orderBy("timestamp", Direction.ASCENDING)
            .snapshots
            .map { snapshot -> 
                snapshot.documents.mapNotNull { doc -> 
                    try {
                        doc.data<Message>()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override fun getMessagesForUser(userId: String): Flow<List<Message>> {
        return messagesCollection
            .where("receiverId", "==", userId)
            .orderBy("timestamp", Direction.DESCENDING)
            .snapshots
            .map { snapshot -> 
                snapshot.documents.mapNotNull { doc -> 
                    try {
                        doc.data<Message>()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override suspend fun markMessageAsRead(messageId: String) {
        messagesCollection.document(messageId).update(mapOf("isRead" to true))
    }

    override suspend fun deleteMessage(messageId: String) {
        messagesCollection.document(messageId).delete()
    }
}
