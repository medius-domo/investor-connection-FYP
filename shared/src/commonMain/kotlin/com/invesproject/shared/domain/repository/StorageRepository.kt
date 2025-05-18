package com.invesproject.shared.domain.repository

interface StorageRepository {
    suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): String
    suspend fun uploadBusinessPlan(proposalId: String, fileBytes: ByteArray, fileName: String): String
    suspend fun deleteFile(path: String)
} 