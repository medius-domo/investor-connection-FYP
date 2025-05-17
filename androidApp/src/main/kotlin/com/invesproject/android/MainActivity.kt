package com.invesproject.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.invesproject.android.ui.screens.auth.LoginScreen
import com.invesproject.android.ui.screens.auth.SignUpScreen
import com.invesproject.android.ui.screens.home.HomeScreen
import com.invesproject.android.ui.theme.InvesProjectTheme
import com.invesproject.shared.data.repository.FirebaseAuthRepository
import com.invesproject.shared.data.repository.FirebaseMessageRepository
import com.invesproject.shared.data.repository.FirebaseProposalRepository
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel

class MainActivity : ComponentActivity() {
    private val authRepository = FirebaseAuthRepository()
    private val proposalRepository = FirebaseProposalRepository()
    private val messageRepository = FirebaseMessageRepository()

    private val authViewModel = AuthViewModel(authRepository)
    private val proposalViewModel = ProposalViewModel(proposalRepository)
    private val messageViewModel = MessageViewModel(messageRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvesProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignUp = { navController.navigate("signup") },
                                onNavigateToHome = { navController.navigate("home") }
                            )
                        }
                        composable("signup") {
                            SignUpScreen(
                                authViewModel = authViewModel,
                                onNavigateToLogin = { navController.popBackStack() },
                                onNavigateToHome = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                authViewModel = authViewModel,
                                proposalViewModel = proposalViewModel,
                                messageViewModel = messageViewModel,
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 