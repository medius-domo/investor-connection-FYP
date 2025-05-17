package com.invesproject.shared.domain.repository

import com.invesproject.shared.domain.model.BusinessProposal
import com.invesproject.shared.domain.model.BusinessSector
import kotlinx.coroutines.flow.Flow

interface ProposalRepository {
    suspend fun createProposal(proposal: BusinessProposal): BusinessProposal
    suspend fun getProposal(id: String): BusinessProposal?
    fun getAllProposals(): Flow<List<BusinessProposal>>
    fun getProposalsBySector(sector: BusinessSector): Flow<List<BusinessProposal>>
    fun getProposalsByInnovator(innovatorId: String): Flow<List<BusinessProposal>>
    suspend fun updateProposal(proposal: BusinessProposal): BusinessProposal
    suspend fun deleteProposal(id: String)
    suspend fun markInterest(proposalId: String, investorId: String)
    suspend fun removeInterest(proposalId: String, investorId: String)
} 