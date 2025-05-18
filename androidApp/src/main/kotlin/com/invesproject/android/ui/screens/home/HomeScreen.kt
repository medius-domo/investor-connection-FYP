package com.invesproject.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.invesproject.android.data.AndroidProposalViewModel
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.shared.presentation.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    proposalViewModel: AndroidProposalViewModel,
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("InvesProject") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.signOut()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Outlined.Message, contentDescription = "Messages") },
                    label = { Text("Messages") }
                )
                if (currentUser?.role == UserRole.INNOVATOR) {
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Add, contentDescription = "New Proposal") },
                        label = { Text("New") }
                    )
                }
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        proposalViewModel = proposalViewModel,
                        onNavigateBack = { selectedTab = 0 }
                    )
                }
                3 -> ProfileScreen(
                    currentUser = currentUser,
                    authViewModel = authViewModel,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }
    }
} 