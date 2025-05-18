package com.invesproject.shared.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class BusinessProposal(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val estimatedBudget: Double = 0.0,
    val sector: BusinessSector = BusinessSector.TECHNOLOGY,
    val innovatorId: String = "",
    val businessPlanUrl: String? = null,
    val businessPlanFileName: String? = null,
    val interestedInvestors: List<String> = emptyList(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
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