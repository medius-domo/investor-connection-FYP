package com.invesproject.android.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.presentation.viewmodel.AuthState
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.shared.ui.components.ErrorMessage
import com.invesproject.shared.ui.components.LoadingSpinner
import com.invesproject.shared.ui.components.OutlinedInput
import com.invesproject.shared.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var roleError by remember { mutableStateOf<String?>(null) }

    val state by authViewModel.state.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedInput(
            value = name,
            onValueChange = {
                name = it
                nameError = null
            },
            label = "Full Name",
            isError = nameError != null,
            errorMessage = nameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedInput(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = "Email",
            isError = emailError != null,
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedInput(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = "Password",
            isError = passwordError != null,
            errorMessage = passwordError,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = "I am a:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UserRole.values().forEach { role ->
                    FilterChip(
                        selected = selectedRole == role,
                        onClick = {
                            selectedRole = role
                            roleError = null
                        },
                        label = { Text(role.name.lowercase().capitalize()) }
                    )
                }
            }
            if (roleError != null) {
                Text(
                    text = roleError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Sign Up",
            onClick = {
                var hasError = false
                if (name.isBlank()) {
                    nameError = "Name is required"
                    hasError = true
                }
                if (email.isBlank()) {
                    emailError = "Email is required"
                    hasError = true
                }
                if (password.isBlank()) {
                    passwordError = "Password is required"
                    hasError = true
                }
                if (selectedRole == null) {
                    roleError = "Please select a role"
                    hasError = true
                }
                if (!hasError) {
                    authViewModel.signUp(email, password, name, selectedRole!!)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Sign In")
        }

        when (state) {
            is AuthState.Loading -> LoadingSpinner()
            is AuthState.Error -> ErrorMessage(
                message = (state as AuthState.Error).message,
                onRetry = {
                    if (selectedRole != null) {
                        authViewModel.signUp(email, password, name, selectedRole!!)
                    }
                }
            )
            else -> Unit
        }
    }
} 