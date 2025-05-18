package com.invesproject.android.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.firebase.storage.FirebaseStorage
import com.invesproject.shared.domain.model.BusinessProposal
import com.invesproject.shared.domain.repository.ProposalRepository
import com.invesproject.shared.domain.repository.StorageRepository
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File

class AndroidProposalViewModel(
    proposalRepository: ProposalRepository,
    storageRepository: StorageRepository
) : ProposalViewModel(proposalRepository, storageRepository) {

    suspend fun downloadAndOpenBusinessPlan(context: Context, proposal: BusinessProposal) {
        val businessPlanUrl = proposal.businessPlanUrl
        val businessPlanFileName = proposal.businessPlanFileName
        
        if (businessPlanUrl == null || businessPlanFileName == null) {
            throw IllegalStateException("No business plan available")
        }

        val storage = FirebaseStorage.getInstance()
        val fileRef = storage.getReferenceFromUrl(businessPlanUrl)
        
        // Create a temporary file
        val file = File(context.cacheDir, businessPlanFileName)
        
        // Download the file
        fileRef.getFile(file).await()
        
        // Get content URI using FileProvider
        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        // Create intent to view the file
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(contentUri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        
        // Start the activity
        context.startActivity(
            Intent.createChooser(intent, "Open Business Plan")
        )
    }
} 