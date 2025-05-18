package com.invesproject.shared.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    val proposalId: String = "",
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val isRead: Boolean = false
) 