package com.invesproject.android.data

import com.google.firebase.storage.FirebaseStorage
import com.invesproject.shared.domain.repository.StorageRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseStorageRepository : StorageRepository {
    private val storage = FirebaseStorage.getInstance()
    private val profilePicturesRef = storage.reference.child("profile_pictures")
    private val businessPlansRef = storage.reference.child("business_plans")

    override suspend fun uploadProfilePicture(userId: String, imageBytes: ByteArray): String {
        val fileRef = profilePicturesRef.child("$userId.jpg")
        return fileRef.putBytes(imageBytes).await().storage.downloadUrl.await().toString()
    }

    override suspend fun uploadBusinessPlan(proposalId: String, fileBytes: ByteArray, fileName: String): String {
        val extension = fileName.substringAfterLast(".", "pdf")
        val fileRef = businessPlansRef.child("$proposalId/$fileName")
        return fileRef.putBytes(fileBytes).await().storage.downloadUrl.await().toString()
    }

    override suspend fun deleteFile(path: String) {
        storage.getReferenceFromUrl(path).delete().await()
    }
} 