package com.invesproject.shared.presentation.viewmodel

import com.invesproject.shared.domain.model.BusinessProposal
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.repository.ProposalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProposalViewModel(
    private val proposalRepository: ProposalRepository,
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
        innovatorId: String,
        businessPlanUrl: String? = null
    ) {
        scope.launch {
            try {
                _state.value = ProposalState.Loading
                val proposal = BusinessProposal(
                    id = "",
                    title = title,
                    description = description,
                    estimatedBudget = estimatedBudget,
                    sector = sector,
                    innovatorId = innovatorId,
                    businessPlanUrl = businessPlanUrl
                )
                proposalRepository.createProposal(proposal)
                _state.value = ProposalState.Success
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to create proposal")
            }
        }
    }

    fun getAllProposals() {
        scope.launch {
            try {
                _state.value = ProposalState.Loading
                proposalRepository.getAllProposals()
                    .catch { e -> _state.value = ProposalState.Error(e.message ?: "Failed to load proposals") }
                    .collect { proposals ->
                        _proposals.value = proposals
                        _state.value = ProposalState.Success
                    }
            } catch (e: Exception) {
                _state.value = ProposalState.Error(e.message ?: "Failed to load proposals")
            }
        }
    }

    fun getProposalsBySector(sector: BusinessSector) {
        scope.launch {
            proposalRepository.getProposalsBySector(sector)
                .catch { e -> _state.value = ProposalState.Error(e.message ?: "Failed to load proposals") }
                .collect { proposals ->
                    _proposals.value = proposals
                }
        }
    }

    fun getProposalsByInnovator(innovatorId: String) {
        scope.launch {
            proposalRepository.getProposalsByInnovator(innovatorId)
                .catch { e -> _state.value = ProposalState.Error(e.message ?: "Failed to load proposals") }
                .collect { proposals ->
                    _proposals.value = proposals
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

    private fun observeProposals() {
        getAllProposals()
    }
}

sealed class ProposalState {
    object Initial : ProposalState()
    object Loading : ProposalState()
    object Success : ProposalState()
    data class Error(val message: String) : ProposalState()
} 