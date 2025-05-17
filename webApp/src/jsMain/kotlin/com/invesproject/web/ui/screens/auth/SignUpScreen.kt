package com.invesproject.web.ui.screens.auth

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.presentation.viewmodel.AuthState
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

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
            Text("Create Account")
        }

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

        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    gap(8.px)
                    width(100.percent)
                }
            }
        ) {
            P(
                attrs = {
                    style {
                        margin(0.px)
                        color(WebColors.OnSurface)
                    }
                }
            ) {
                Text("I am a:")
            }

            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Flex)
                        gap(8.px)
                    }
                }
            ) {
                UserRole.values().forEach { role ->
                    Button(
                        attrs = {
                            onClick { selectedRole = role }
                            style {
                                backgroundColor(if (selectedRole == role) WebColors.Primary else Color.transparent)
                                color(if (selectedRole == role) WebColors.OnPrimary else WebColors.Primary)
                                border(1.px, LineStyle.Solid, WebColors.Primary)
                                borderRadius(4.px)
                                padding(8.px, 16.px)
                                cursor("pointer")
                                fontSize(14.px)
                            }
                        }
                    ) {
                        Text(role.name.lowercase().capitalize())
                    }
                }
            }

            if (roleError != null) {
                Span(
                    attrs = {
                        style {
                            color(WebColors.Error)
                            fontSize(12.px)
                        }
                    }
                ) {
                    Text(roleError!!)
                }
            }
        }

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
            },
            modifier = {
                width(100.percent)
            }
        )

        Button(
            attrs = {
                onClick { onNavigateToLogin() }
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