package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalState
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun NewProposalScreen(
    currentUser: User,
    proposalViewModel: ProposalViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var estimatedBudget by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf<BusinessSector?>(null) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var budgetError by remember { mutableStateOf<String?>(null) }
    var sectorError by remember { mutableStateOf<String?>(null) }

    val state by proposalViewModel.state.collectAsState()

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(24.px)
                width(100.percent)
            }
        }
    ) {
        H2(
            attrs = {
                classes(WebTypography.TitleLarge)
                style {
                    margin(0.px)
                }
            }
        ) {
            Text("Create New Proposal")
        }

        Card {
            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Column)
                        gap(16.px)
                    }
                }
            ) {
                OutlinedInput(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = null
                    },
                    label = "Title",
                    isError = titleError != null,
                    errorMessage = titleError
                )

                OutlinedInput(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = null
                    },
                    label = "Description",
                    isError = descriptionError != null,
                    errorMessage = descriptionError
                )

                OutlinedInput(
                    value = estimatedBudget,
                    onValueChange = {
                        estimatedBudget = it
                        budgetError = null
                    },
                    label = "Estimated Budget",
                    type = "number",
                    isError = budgetError != null,
                    errorMessage = budgetError
                )

                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Column)
                            gap(8.px)
                        }
                    }
                ) {
                    Label(
                        attrs = {
                            style {
                                color(if (sectorError != null) WebColors.Error else WebColors.OnSurface)
                                fontSize(12.px)
                            }
                        }
                    ) {
                        Text("Business Sector")
                    }

                    Select(
                        attrs = {
                            onChange { event -> 
                                selectedSector = BusinessSector.valueOf(event.value)
                                sectorError = null
                            }
                            style {
                                width(100.percent)
                                padding(12.px)
                                borderRadius(4.px)
                                border(1.px, LineStyle.Solid, if (sectorError != null) WebColors.Error else WebColors.OnSurface)
                                fontSize(14.px)
                            }
                        }
                    ) {
                        Option(
                            attrs = {
                                value("")
                            }
                        ) {
                            Text("Select a sector")
                        }
                        BusinessSector.values().forEach { sector ->
                            Option(
                                attrs = {
                                    value(sector.name)
                                }
                            ) {
                                Text(sector.name)
                            }
                        }
                    }
                    if (sectorError != null) {
                        Span(
                            attrs = {
                                style {
                                    color(WebColors.Error)
                                    fontSize(12.px)
                                }
                            }
                        ) {
                            Text(sectorError!!)
                        }
                    }
                }

                PrimaryButton(
                    text = "Create Proposal",
                    onClick = {
                        var hasError = false
                        if (title.isBlank()) {
                            titleError = "Title is required"
                            hasError = true
                        }
                        if (description.isBlank()) {
                            descriptionError = "Description is required"
                            hasError = true
                        }
                        if (estimatedBudget.isBlank()) {
                            budgetError = "Budget is required"
                            hasError = true
                        }
                        if (selectedSector == null) {
                            sectorError = "Please select a business sector"
                            hasError = true
                        }
                        if (!hasError) {
                            proposalViewModel.createProposal(
                                title = title,
                                description = description,
                                estimatedBudget = estimatedBudget.toDouble(),
                                sector = selectedSector!!,
                                innovatorId = currentUser.id
                            )
                        }
                    },
                    modifier = {
                        width(100.percent)
                    }
                )

                when (state) {
                    is ProposalState.Loading -> LoadingSpinner()
                    is ProposalState.Error -> ErrorMessage(
                        message = (state as ProposalState.Error).message,
                        onRetry = {
                            if (title.isNotBlank() && description.isNotBlank() && 
                                estimatedBudget.isNotBlank() && selectedSector != null) {
                                proposalViewModel.createProposal(
                                    title = title,
                                    description = description,
                                    estimatedBudget = estimatedBudget.toDouble(),
                                    sector = selectedSector!!,
                                    innovatorId = currentUser.id
                                )
                            }
                        }
                    )
                    is ProposalState.Success -> {
                        LaunchedEffect(Unit) {
                            title = ""
                            description = ""
                            estimatedBudget = ""
                            selectedSector = null
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
} 