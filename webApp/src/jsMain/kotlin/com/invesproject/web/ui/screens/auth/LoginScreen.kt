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
                    Text("Welcome Back")
                }

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

                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            justifyContent(JustifyContent.Center)
                            gap(8.px)
                        }
                    }
                ) {
                    Text("Don't have an account?")
                    Button(
                        attrs = {
                            onClick { onNavigateToSignUp() }
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
                        Text("Sign Up")
                    }
                }

                when (state) {
                    is AuthState.Loading -> LoadingSpinner()
                    is AuthState.Error -> ErrorMessage(
                        message = (state as AuthState.Error).message,
                        onRetry = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                authViewModel.signIn(email, password)
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