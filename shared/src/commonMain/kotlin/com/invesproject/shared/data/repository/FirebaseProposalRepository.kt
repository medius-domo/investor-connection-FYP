package com.invesproject.shared.data.repository

import com.invesproject.shared.domain.model.BusinessProposal
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.repository.ProposalRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import dev.gitlive.firebase.firestore.orderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseProposalRepository : ProposalRepository {
    private val firestore = Firebase.firestore
    private val proposalsCollection = firestore.collection("proposals")

    override suspend fun createProposal(proposal: BusinessProposal): BusinessProposal {
        val docRef = proposalsCollection.document("")
        val proposalWithId = proposal.copy(id = docRef.id)
        docRef.set(proposalWithId)
        return proposalWithId
    }

    override suspend fun getProposal(id: String): BusinessProposal? {
        return try {
            proposalsCollection.document(id).get().data<BusinessProposal>()
        } catch (e: Exception) {
            null
        }
    }

    override fun getAllProposals(): Flow<List<BusinessProposal>> {
        return proposalsCollection
            .orderBy("createdAt", Direction.DESCENDING)
            .snapshots
            .map { snapshot -> 
                snapshot.documents.mapNotNull { doc -> 
                    try {
                        doc.data<BusinessProposal>()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override fun getProposalsBySector(sector: BusinessSector): Flow<List<BusinessProposal>> {
        return proposalsCollection
            .where("sector", "==", sector)
            .orderBy("createdAt", Direction.DESCENDING)
            .snapshots
            .map { snapshot -> 
                snapshot.documents.mapNotNull { doc -> 
                    try {
                        doc.data<BusinessProposal>()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override fun getProposalsByInnovator(innovatorId: String): Flow<List<BusinessProposal>> {
        return proposalsCollection
            .where("innovatorId", "==", innovatorId)
            .orderBy("createdAt", Direction.DESCENDING)
            .snapshots
            .map { snapshot -> 
                snapshot.documents.mapNotNull { doc -> 
                    try {
                        doc.data<BusinessProposal>()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }

    override suspend fun updateProposal(proposal: BusinessProposal): BusinessProposal {
        proposalsCollection.document(proposal.id).set(proposal)
        return proposal
    }

    override suspend fun deleteProposal(id: String) {
        proposalsCollection.document(id).delete()
    }

    override suspend fun markInterest(proposalId: String, investorId: String) {
        val proposal = getProposal(proposalId) ?: throw IllegalStateException("Proposal not found")
        val updatedProposal = proposal.copy(
            interestedInvestors = proposal.interestedInvestors + investorId
        )
        updateProposal(updatedProposal)
    }

    override suspend fun removeInterest(proposalId: String, investorId: String) {
        val proposal = getProposal(proposalId) ?: throw IllegalStateException("Proposal not found")
        val updatedProposal = proposal.copy(
            interestedInvestors = proposal.interestedInvestors - investorId
        )
        updateProposal(updatedProposal)
    }
} 