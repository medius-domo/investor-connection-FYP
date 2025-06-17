package com.invesproject.web.ui

import androidx.compose.runtime.*
import com.invesproject.shared.data.repository.*
import com.invesproject.shared.presentation.viewmodel.*
import com.invesproject.web.ui.screens.auth.LoginScreen
import com.invesproject.web.ui.screens.auth.SignUpScreen
import com.invesproject.web.ui.screens.home.HomeScreen
import com.invesproject.web.ui.theme.InvesProjectTheme
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div

@Composable
fun App() {
    Style(InvesProjectTheme)
    
    var currentScreen by remember { mutableStateOf("login") }
    
    val authRepository = remember { FirebaseAuthRepository() }
    val storageRepository = remember { FirebaseStorageRepository() }
    val proposalRepository = remember { FirebaseProposalRepository() }
    val messageRepository = remember { FirebaseMessageRepository() }
    
    val authViewModel = remember { AuthViewModel(authRepository, storageRepository) }
    val proposalViewModel = remember { ProposalViewModel(proposalRepository, storageRepository) }
    val messageViewModel = remember { MessageViewModel(messageRepository) }
    
    Div {
        when (currentScreen) {
            "login" -> LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { currentScreen = "signup" },
                onNavigateToHome = { currentScreen = "home" }
            )
            "signup" -> SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { currentScreen = "login" },
                onNavigateToHome = { currentScreen = "home" }
            )
            "home" -> HomeScreen(
                authViewModel = authViewModel,
                proposalViewModel = proposalViewModel,
                messageViewModel = messageViewModel,
                onNavigateToLogin = { currentScreen = "login" }
            )
        }
    }
} 