package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.ProposalState
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun NewProposalScreen(
    currentUser: User?,
    proposalViewModel: ProposalViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
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
                maxWidth(600.px)
                margin(0.px, Auto.auto)
                width(100.percent)
                padding(16.px)
            }
        }
    ) {
        H2(
            attrs = {
                classes(WebTypography.HeadlineMedium)
                style {
                    color(WebColors.Primary)
                    margin(0.px)
                }
            }
        ) {
            Text("New Business Proposal")
        }

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

        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    gap(4.px)
                }
            }
        ) {
            Label(
                attrs = {
                    style {
                        color(if (descriptionError != null) WebColors.Error else WebColors.OnSurface)
                        fontSize(12.px)
                    }
                }
            ) {
                Text("Description")
            }
            TextArea(
                attrs = {
                    onInput { event -> 
                        description = event.value
                        descriptionError = null
                    }
                    style {
                        width(100.percent)
                        minHeight(120.px)
                        padding(12.px)
                        borderRadius(4.px)
                        border(1.px, LineStyle.Solid, if (descriptionError != null) WebColors.Error else WebColors.OnSurface)
                        boxSizing("border-box")
                        fontSize(14.px)
                        property("resize", "vertical")
                    }
                }
            ) {
                Text(description)
            }
            if (descriptionError != null) {
                Span(
                    attrs = {
                        style {
                            color(WebColors.Error)
                            fontSize(12.px)
                        }
                    }
                ) {
                    Text(descriptionError!!)
                }
            }
        }

        OutlinedInput(
            value = budget,
            onValueChange = {
                budget = it.filter { char -> char.isDigit() || char == '.' }
                budgetError = null
            },
            label = "Estimated Budget ($)",
            type = InputType.Number,
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
            text = "Submit Proposal",
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
                if (budget.isBlank()) {
                    budgetError = "Budget is required"
                    hasError = true
                }
                if (selectedSector == null) {
                    sectorError = "Please select a sector"
                    hasError = true
                }

                if (!hasError && currentUser != null) {
                    proposalViewModel.createProposal(
                        title = title,
                        description = description,
                        estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
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
                    if (currentUser != null && selectedSector != null) {
                        proposalViewModel.createProposal(
                            title = title,
                            description = description,
                            estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
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
                    budget = ""
                    selectedSector = null
                }
                Div(
                    attrs = {
                        style {
                            color(WebColors.Secondary)
                            textAlign("center")
                            padding(16.px)
                        }
                    }
                ) {
                    Text("Proposal submitted successfully!")
                }
            }
            else -> Unit
        }
    }
} 