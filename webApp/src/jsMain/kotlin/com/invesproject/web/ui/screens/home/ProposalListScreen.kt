package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalState
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun ProposalListScreen(
    currentUser: User?,
    proposalViewModel: ProposalViewModel,
    messageViewModel: MessageViewModel
) {
    var selectedSector by remember { mutableStateOf<BusinessSector?>(null) }
    var showContactDialog by remember { mutableStateOf(false) }
    var selectedProposalId by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }

    val proposals by proposalViewModel.proposals.collectAsState()
    val state by proposalViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (currentUser?.role == UserRole.INNOVATOR) {
            proposalViewModel.getProposalsByInnovator(currentUser.id)
        } else {
            proposalViewModel.getAllProposals()
        }
    }

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(16.px)
            }
        }
    ) {
        if (currentUser?.role == UserRole.INVESTOR) {
            // Sector filter
            Select(
                attrs = {
                    onChange { event -> 
                        val sector = if (event.value == "All Sectors") null 
                        else BusinessSector.valueOf(event.value)
                        selectedSector = sector
                        if (sector != null) {
                            proposalViewModel.getProposalsBySector(sector)
                        } else {
                            proposalViewModel.getAllProposals()
                        }
                    }
                    style {
                        padding(8.px)
                        borderRadius(4.px)
                        border(1.px, LineStyle.Solid, WebColors.Primary)
                        width(200.px)
                    }
                }
            ) {
                Option(
                    attrs = {
                        value("All Sectors")
                    }
                ) {
                    Text("All Sectors")
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
        }

        when (state) {
            is ProposalState.Loading -> LoadingSpinner()
            is ProposalState.Error -> ErrorMessage(
                message = (state as ProposalState.Error).message,
                onRetry = {
                    if (selectedSector != null) {
                        proposalViewModel.getProposalsBySector(selectedSector!!)
                    } else {
                        proposalViewModel.getAllProposals()
                    }
                }
            )
            else -> {
                if (proposals.isEmpty()) {
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                justifyContent(JustifyContent.Center)
                                alignItems(AlignItems.Center)
                                padding(32.px)
                            }
                        }
                    ) {
                        Text("No proposals found")
                    }
                } else {
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Grid)
                                gridTemplateColumns("repeat(auto-fill, minmax(300px, 1fr))")
                                gap(16.px)
                            }
                        }
                    ) {
                        proposals.forEach { proposal ->
                            Card(
                                modifier = {
                                    cursor("pointer")
                                    property("transition", "transform 0.2s")
                                    hover {
                                        property("transform", "translateY(-4px)")
                                    }
                                }
                            ) {
                                Div(
                                    attrs = {
                                        style {
                                            display(DisplayStyle.Flex)
                                            flexDirection(FlexDirection.Column)
                                            gap(8.px)
                                        }
                                    }
                                ) {
                                    H3(
                                        attrs = {
                                            classes(WebTypography.TitleLarge)
                                            style {
                                                margin(0.px)
                                                color(WebColors.Primary)
                                            }
                                        }
                                    ) {
                                        Text(proposal.title)
                                    }
                                    P {
                                        Text(proposal.description)
                                    }
                                    P(
                                        attrs = {
                                            style {
                                                color(WebColors.Secondary)
                                                fontWeight("500")
                                            }
                                        }
                                    ) {
                                        Text("Budget: $${proposal.estimatedBudget}")
                                    }
                                    P(
                                        attrs = {
                                            style {
                                                color(WebColors.OnSurfaceVariant)
                                            }
                                        }
                                    ) {
                                        Text("Sector: ${proposal.sector.name}")
                                    }

                                    if (currentUser?.role == UserRole.INVESTOR) {
                                        PrimaryButton(
                                            text = "Contact Innovator",
                                            onClick = {
                                                selectedProposalId = proposal.id
                                                showContactDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showContactDialog && selectedProposalId != null) {
        // Contact Dialog
        Div(
            attrs = {
                style {
                    position(Position.Fixed)
                    top(0.px)
                    left(0.px)
                    right(0.px)
                    bottom(0.px)
                    backgroundColor(Color("rgba(0, 0, 0, 0.5)"))
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.Center)
                    alignItems(AlignItems.Center)
                    zIndex(2)
                }
            }
        ) {
            Card(
                modifier = {
                    width(400.px)
                    maxWidth(90.percent)
                }
            ) {
                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Column)
                            gap(16.px)
                        }
                    }
                ) {
                    H3(
                        attrs = {
                            classes(WebTypography.TitleLarge)
                            style {
                                margin(0.px)
                                color(WebColors.Primary)
                            }
                        }
                    ) {
                        Text("Contact Innovator")
                    }

                    TextArea(
                        attrs = {
                            onInput { event -> messageText = event.value }
                            style {
                                width(100.percent)
                                minHeight(100.px)
                                padding(8.px)
                                borderRadius(4.px)
                                border(1.px, LineStyle.Solid, WebColors.Primary)
                                boxSizing("border-box")
                            }
                        }
                    ) {
                        Text(messageText)
                    }

                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                justifyContent(JustifyContent.FlexEnd)
                                gap(8.px)
                            }
                        }
                    ) {
                        Button(
                            attrs = {
                                onClick {
                                    showContactDialog = false
                                    selectedProposalId = null
                                    messageText = ""
                                }
                                style {
                                    backgroundColor(Color.transparent)
                                    color(WebColors.Primary)
                                    border(0.px)
                                    cursor("pointer")
                                    padding(8.px, 16.px)
                                }
                            }
                        ) {
                            Text("Cancel")
                        }

                        PrimaryButton(
                            text = "Send",
                            onClick = {
                                if (messageText.isNotBlank() && currentUser != null) {
                                    val proposal = proposals.find { it.id == selectedProposalId }
                                    if (proposal != null) {
                                        messageViewModel.sendMessage(
                                            senderId = currentUser.id,
                                            receiverId = proposal.innovatorId,
                                            content = messageText,
                                            proposalId = proposal.id
                                        )
                                        proposalViewModel.markInterest(proposal.id, currentUser.id)
                                    }
                                }
                                showContactDialog = false
                                selectedProposalId = null
                                messageText = ""
                            }
                        )
                    }
                }
            }
        }
    }
} 