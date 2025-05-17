package com.invesproject.shared.domain.repository

import com.invesproject.shared.domain.model.User
import com.invesproject.shared.domain.model.UserRole

interface AuthRepository {
    suspend fun signUp(email: String, password: String, name: String, role: UserRole): User
    suspend fun signIn(email: String, password: String): User
    suspend fun signOut()
    suspend fun getCurrentUser(): User?
    suspend fun updateUserProfile(user: User): User
} 