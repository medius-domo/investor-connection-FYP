package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    proposalViewModel: ProposalViewModel,
    messageViewModel: MessageViewModel,
    onNavigateToLogin: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                minHeight(100.vh)
            }
        }
    ) {
        // Header
        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.SpaceBetween)
                    alignItems(AlignItems.Center)
                    padding(16.px)
                    backgroundColor(WebColors.Primary)
                    color(WebColors.OnPrimary)
                    property("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
                    property("z-index", "1")
                }
            }
        ) {
            H1(
                attrs = {
                    classes(WebTypography.HeadlineLarge)
                    style {
                        margin(0.px)
                    }
                }
            ) {
                Text("InvesProject")
            }
            if (currentUser != null) {
                Button(
                    attrs = {
                        onClick { authViewModel.signOut() }
                        style {
                            backgroundColor(Color.transparent)
                            color(WebColors.OnPrimary)
                            border(0.px)
                            cursor("pointer")
                            padding(8.px, 16.px)
                        }
                    }
                ) {
                    Text("Sign Out")
                }
            }
        }

        // Main Content
        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flex(1)
                    padding(16.px)
                    maxWidth(1200.px)
                    margin(0.px)
                    width(100.percent)
                }
            }
        ) {
            when (val user = currentUser) {
                null -> {
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                justifyContent(JustifyContent.Center)
                                alignItems(AlignItems.Center)
                                width(100.percent)
                            }
                        }
                    ) {
                        Text("Please sign in to continue")
                    }
                }
                else -> {
                    when (user.role) {
                        UserRole.INNOVATOR -> {
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
                                NewProposalScreen(
                                    currentUser = user,
                                    proposalViewModel = proposalViewModel
                                )
                                ProposalListScreen(
                                    currentUser = user,
                                    proposalViewModel = proposalViewModel,
                                    messageViewModel = messageViewModel
                                )
                            }
                        }
                        UserRole.INVESTOR -> {
                            ProposalListScreen(
                                currentUser = user,
                                proposalViewModel = proposalViewModel,
                                messageViewModel = messageViewModel
                            )
                        }
                    }
                    MessageListScreen(
                        currentUser = user,
                        messageViewModel = messageViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun NavButton(
    text: String,
    tab: Int,
    selectedTab: Int,
    onClick: () -> Unit
) {
    Button(
        attrs = {
            onClick { onClick() }
            style {
                backgroundColor(if (selectedTab == tab) WebColors.Primary else Color.transparent)
                color(if (selectedTab == tab) WebColors.OnPrimary else WebColors.Primary)
                border(1.px, LineStyle.Solid, WebColors.Primary)
                borderRadius(4.px)
                padding(8.px, 16.px)
                cursor("pointer")
                fontSize(14.px)
                property("transition", "all 0.2s")
            }
        }
    ) {
        Text(text)
    }
} 