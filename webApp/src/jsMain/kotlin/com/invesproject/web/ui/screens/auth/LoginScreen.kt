package com.invesproject.web.ui.screens.auth

import androidx.compose.runtime.*
import com.invesproject.shared.presentation.viewmodel.AuthState
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val state by authViewModel.state.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onNavigateToHome()
        }
    }

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
                minHeight(100.vh)
                padding(16.px)
                gap(16.px)
                maxWidth(400.px)
                margin(0.px, Auto.auto)
            }
        }
    ) {
        H1(
            attrs = {
                classes(WebTypography.HeadlineLarge)
                style {
                    color(WebColors.Primary)
                    margin(0.px)
                }
            }
        ) {
            Text("Welcome Back")
        }

        OutlinedInput(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = "Email",
            type = InputType.Email,
            isError = emailError != null,
            errorMessage = emailError
        )

        OutlinedInput(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = "Password",
            type = InputType.Password,
            isError = passwordError != null,
            errorMessage = passwordError
        )

        PrimaryButton(
            text = "Sign In",
            onClick = {
                var hasError = false
                if (email.isBlank()) {
                    emailError = "Email is required"
                    hasError = true
                }
                if (password.isBlank()) {
                    passwordError = "Password is required"
                    hasError = true
                }
                if (!hasError) {
                    authViewModel.signIn(email, password)
                }
            },
            modifier = {
                width(100.percent)
            }
        )

        Button(
            attrs = {
                onClick { onNavigateToSignUp() }
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
            Text("Don't have an account? Sign Up")
        }

        when (state) {
            is AuthState.Loading -> LoadingSpinner()
            is AuthState.Error -> ErrorMessage(
                message = (state as AuthState.Error).message,
                onRetry = { authViewModel.signIn(email, password) }
            )
            else -> Unit
        }
    }
} 