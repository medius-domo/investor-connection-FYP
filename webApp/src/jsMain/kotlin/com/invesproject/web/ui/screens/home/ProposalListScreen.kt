package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.Proposal
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalState
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun ProposalListScreen(
    currentUser: User,
    proposalViewModel: ProposalViewModel,
    messageViewModel: MessageViewModel
) {
    var selectedProposal by remember { mutableStateOf<Proposal?>(null) }
    var messageText by remember { mutableStateOf("") }
    var showContactDialog by remember { mutableStateOf(false) }

    val state by proposalViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        proposalViewModel.getProposals()
    }

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
            Text("Business Proposals")
        }

        when (state) {
            is ProposalState.Loading -> LoadingSpinner()
            is ProposalState.Error -> ErrorMessage(
                message = (state as ProposalState.Error).message,
                onRetry = { proposalViewModel.getProposals() }
            )
            is ProposalState.Success -> {
                val proposals = (state as ProposalState.Success).proposals
                if (proposals.isEmpty()) {
                    Text("No proposals found")
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
                                    property("&:hover", "transform: translateY(-4px)")
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
                                            classes(WebTypography.TitleMedium)
                                            style {
                                                margin(0.px)
                                            }
                                        }
                                    ) {
                                        Text(proposal.title)
                                    }
                                    P(
                                        attrs = {
                                            classes(WebTypography.BodyMedium)
                                            style {
                                                margin(0.px)
                                                color(WebColors.OnSurfaceVariant)
                                            }
                                        }
                                    ) {
                                        Text(proposal.description)
                                    }
                                    Div(
                                        attrs = {
                                            style {
                                                display(DisplayStyle.Flex)
                                                justifyContent(JustifyContent.SpaceBetween)
                                                alignItems(AlignItems.Center)
                                            }
                                        }
                                    ) {
                                        Span(
                                            attrs = {
                                                classes(WebTypography.LabelMedium)
                                                style {
                                                    color(WebColors.Primary)
                                                }
                                            }
                                        ) {
                                            Text("Budget: $${proposal.estimatedBudget}")
                                        }
                                        if (currentUser.role.name == "INVESTOR") {
                                            PrimaryButton(
                                                text = "Contact",
                                                onClick = {
                                                    selectedProposal = proposal
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
            else -> Unit
        }
    }

    if (showContactDialog && selectedProposal != null) {
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
                    property("z-index", "1000")
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
                            classes(WebTypography.TitleMedium)
                            style {
                                margin(0.px)
                            }
                        }
                    ) {
                        Text("Contact Innovator")
                    }
                    OutlinedInput(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = "Message"
                    )
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
                                onClick { showContactDialog = false }
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
                                if (messageText.isNotBlank()) {
                                    messageViewModel.sendMessage(
                                        senderId = currentUser.id,
                                        receiverId = selectedProposal!!.innovatorId,
                                        content = messageText,
                                        proposalId = selectedProposal!!.id
                                    )
                                    messageText = ""
                                    showContactDialog = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
} 