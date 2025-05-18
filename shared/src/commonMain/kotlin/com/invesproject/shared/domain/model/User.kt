package com.invesproject.shared.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.INNOVATOR,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)

@Serializable
enum class UserRole {
    INNOVATOR,
    INVESTOR
} 