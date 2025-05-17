package com.invesproject.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
enum class UserRole {
    INNOVATOR,
    INVESTOR
} 