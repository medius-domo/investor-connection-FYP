package com.invesproject.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessProposal(
    val id: String,
    val title: String,
    val description: String,
    val estimatedBudget: Double,
    val sector: BusinessSector,
    val innovatorId: String,
    val businessPlanUrl: String? = null,
    val interestedInvestors: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
enum class BusinessSector {
    TECHNOLOGY,
    HEALTHCARE,
    FINANCE,
    EDUCATION,
    RETAIL,
    MANUFACTURING,
    AGRICULTURE,
    OTHER
} 