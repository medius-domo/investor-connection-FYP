package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
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
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            onNavigateToLogin()
        }
    }

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
        Header(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.SpaceBetween)
                    alignItems(AlignItems.Center)
                    padding(16.px)
                    backgroundColor(WebColors.Surface)
                    boxShadow("0 2px 4px rgba(0,0,0,0.1)")
                    position(Position.Sticky)
                    top(0.px)
                    zIndex(1)
                }
            }
        ) {
            H1(
                attrs = {
                    classes(WebTypography.TitleLarge)
                    style {
                        color(WebColors.Primary)
                        margin(0.px)
                    }
                }
            ) {
                Text("InvesProject")
            }

            Button(
                attrs = {
                    onClick { authViewModel.signOut() }
                    style {
                        backgroundColor(Color.transparent)
                        color(WebColors.Primary)
                        border(0.px)
                        cursor("pointer")
                        fontSize(14.px)
                        padding(8.px)
                    }
                }
            ) {
                Text("Sign Out")
            }
        }

        // Main content
        Main(
            attrs = {
                style {
                    flex(1)
                    padding(16.px)
                    maxWidth(1200.px)
                    margin(0.px, Auto.auto)
                    width(100.percent)
                    boxSizing("border-box")
                }
            }
        ) {
            when (selectedTab) {
                0 -> ProposalListScreen(
                    currentUser = currentUser,
                    proposalViewModel = proposalViewModel,
                    messageViewModel = messageViewModel
                )
                1 -> MessageListScreen(
                    currentUser = currentUser,
                    messageViewModel = messageViewModel
                )
                2 -> if (currentUser?.role == UserRole.INNOVATOR) {
                    NewProposalScreen(
                        currentUser = currentUser,
                        proposalViewModel = proposalViewModel
                    )
                }
                3 -> ProfileScreen(
                    currentUser = currentUser,
                    authViewModel = authViewModel
                )
            }
        }

        // Navigation
        Nav(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.Center)
                    gap(8.px)
                    padding(16.px)
                    backgroundColor(WebColors.Surface)
                    boxShadow("0 -2px 4px rgba(0,0,0,0.1)")
                }
            }
        ) {
            NavButton("Home", 0, selectedTab) { selectedTab = 0 }
            NavButton("Messages", 1, selectedTab) { selectedTab = 1 }
            if (currentUser?.role == UserRole.INNOVATOR) {
                NavButton("New Proposal", 2, selectedTab) { selectedTab = 2 }
            }
            NavButton("Profile", 3, selectedTab) { selectedTab = 3 }
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