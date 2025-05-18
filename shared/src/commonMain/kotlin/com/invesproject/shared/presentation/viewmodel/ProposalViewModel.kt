package com.invesproject.shared.presentation.viewmodel

import com.invesproject.shared.domain.model.BusinessProposal
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.repository.ProposalRepository
import com.invesproject.shared.domain.repository.StorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class ProposalViewModel(
    private val proposalRepository: ProposalRepository,
    private val storageRepository: StorageRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _state = MutableStateFlow<ProposalState>(ProposalState.Initial)
    val state: StateFlow<ProposalState> = _state.asStateFlow()

    private val _proposals = MutableStateFlow<List<BusinessProposal>>(emptyList())
    val proposals: StateFlow<List<BusinessProposal>> = _proposals.asStateFlow()

    init {
        observeProposals()
    }

    fun createProposal(
        title: String,
        description: String,
        estimatedBudget: Double,
        sector: BusinessSector,
        innovatorId: String
    ) {
        scope.launch {
            _state.value = ProposalState.Loading
            try {
                val proposal = BusinessProposal(
                    title = title,
                    description = description,
                    estimatedBudget = estimatedBudget,
                    sector = sector,
                    innovatorId = innovatorId
                )
                proposalRepository.createProposal(proposal)
                _state.value = ProposalState.Success
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to create proposal")
            }
        }
    }

    fun createProposalWithFile(
        title: String,
        description: String,
        estimatedBudget: Double,
        sector: BusinessSector,
        innovatorId: String,
        fileBytes: ByteArray,
        fileName: String
    ) {
        scope.launch {
            _state.value = ProposalState.Loading
            try {
                // First create the proposal
                val proposal = BusinessProposal(
                    title = title,
                    description = description,
                    estimatedBudget = estimatedBudget,
                    sector = sector,
                    innovatorId = innovatorId
                )
                val createdProposal = proposalRepository.createProposal(proposal)

                // Upload the business plan
                val fileUrl = storageRepository.uploadBusinessPlan(
                    proposalId = createdProposal.id,
                    fileBytes = fileBytes,
                    fileName = fileName
                )

                // Update the proposal with the file URL
                val updatedProposal = createdProposal.copy(
                    businessPlanUrl = fileUrl,
                    businessPlanFileName = fileName
                )
                proposalRepository.updateProposal(updatedProposal)

                _state.value = ProposalState.Success
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to create proposal with file")
            }
        }
    }

    fun downloadBusinessPlan(proposal: BusinessProposal): Flow<ByteArray> = flow {
        if (proposal.businessPlanUrl == null) {
            throw IllegalStateException("No business plan available")
        }
        // Download logic will be implemented in platform-specific code
        throw NotImplementedError("Download not implemented for this platform")
    }

    private fun observeProposals() {
        scope.launch {
            try {
                proposalRepository.getAllProposals()
                    .catch { e -> 
                        _state.value = ProposalState.Error(e.message ?: "Failed to load proposals")
                    }
                    .collect { proposalList ->
                        _proposals.value = proposalList
                    }
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to observe proposals")
            }
        }
    }

    fun getProposalsBySector(sector: BusinessSector) {
        scope.launch {
            try {
                proposalRepository.getProposalsBySector(sector)
                    .catch { e -> 
                        _state.value = ProposalState.Error(e.message ?: "Failed to load proposals")
                    }
                    .collect { proposalList ->
                        _proposals.value = proposalList
                    }
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to load proposals by sector")
            }
        }
    }

    fun getProposalsByInnovator(innovatorId: String) {
        scope.launch {
            try {
                proposalRepository.getProposalsByInnovator(innovatorId)
                    .catch { e -> 
                        _state.value = ProposalState.Error(e.message ?: "Failed to load proposals")
                    }
                    .collect { proposalList ->
                        _proposals.value = proposalList
                    }
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to load proposals by innovator")
            }
        }
    }

    fun markInterest(proposalId: String, investorId: String) {
        scope.launch {
            try {
                _state.value = ProposalState.Loading
                proposalRepository.markInterest(proposalId, investorId)
                _state.value = ProposalState.Success
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to mark interest")
            }
        }
    }
}

sealed class ProposalState {
    object Initial : ProposalState()
    object Loading : ProposalState()
    object Success : ProposalState()
    data class Error(val message: String) : ProposalState()
} 