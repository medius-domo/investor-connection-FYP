package com.invesproject.shared.domain.repository

interface StorageRepository {
    suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): String
    suspend fun deleteFile(fileUrl: String)
    suspend fun uploadBusinessPlan(proposalId: String, fileBytes: ByteArray, fileName: String): String
    // Add other storage-related methods as needed
} 