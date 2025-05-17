package com.invesproject.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val proposalId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
) 