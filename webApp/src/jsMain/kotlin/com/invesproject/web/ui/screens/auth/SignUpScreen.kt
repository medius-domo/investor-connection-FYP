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
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
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
                        gap(24.px)
                    }
                }
            ) {
                H1(
                    attrs = {
                        classes(WebTypography.HeadlineLarge)
                        style {
                            color(WebColors.Primary)
                            margin(0.px)
                            textAlign("center")
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
                    type = "email",
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
                    type = "password",
                    isError = passwordError != null,
                    errorMessage = passwordError
                )

                OutlinedInput(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    label = "Confirm Password",
                    type = "password",
                    isError = confirmPasswordError != null,
                    errorMessage = confirmPasswordError
                )

                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Column)
                            gap(8.px)
                        }
                    }
                ) {
                    Label(
                        attrs = {
                            style {
                                color(if (roleError != null) WebColors.Error else WebColors.OnSurface)
                                fontSize(12.px)
                            }
                        }
                    ) {
                        Text("Role")
                    }

                    Select(
                        attrs = {
                            onChange { event -> 
                                selectedRole = UserRole.valueOf(event.value)
                                roleError = null
                            }
                            style {
                                width(100.percent)
                                padding(12.px)
                                borderRadius(4.px)
                                border(1.px, LineStyle.Solid, if (roleError != null) WebColors.Error else WebColors.OnSurface)
                                fontSize(14.px)
                            }
                        }
                    ) {
                        Option(
                            attrs = {
                                value("")
                            }
                        ) {
                            Text("Select a role")
                        }
                        UserRole.values().forEach { role ->
                            Option(
                                attrs = {
                                    value(role.name)
                                }
                            ) {
                                Text(role.name)
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
                        if (confirmPassword.isBlank()) {
                            confirmPasswordError = "Please confirm your password"
                            hasError = true
                        }
                        if (password != confirmPassword) {
                            confirmPasswordError = "Passwords do not match"
                            hasError = true
                        }
                        if (selectedRole == null) {
                            roleError = "Please select a role"
                            hasError = true
                        }
                        if (!hasError) {
                            authViewModel.signUp(name, email, password, selectedRole!!)
                        }
                    },
                    modifier = {
                        width(100.percent)
                    }
                )

                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            justifyContent(JustifyContent.Center)
                            gap(8.px)
                        }
                    }
                ) {
                    Text("Already have an account?")
                    Button(
                        attrs = {
                            onClick { onNavigateToLogin() }
                            style {
                                backgroundColor(Color.transparent)
                                color(WebColors.Primary)
                                border(0.px)
                                cursor("pointer")
                                padding(0.px)
                                fontSize(14.px)
                            }
                        }
                    ) {
                        Text("Sign In")
                    }
                }

                when (state) {
                    is AuthState.Loading -> LoadingSpinner()
                    is AuthState.Error -> ErrorMessage(
                        message = (state as AuthState.Error).message,
                        onRetry = {
                            if (name.isNotBlank() && email.isNotBlank() && 
                                password.isNotBlank() && confirmPassword.isNotBlank() && 
                                selectedRole != null) {
                                authViewModel.signUp(name, email, password, selectedRole!!)
                            }
                        }
                    )
                    is AuthState.Success -> {
                        LaunchedEffect(Unit) {
                            onNavigateToHome()
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
} 