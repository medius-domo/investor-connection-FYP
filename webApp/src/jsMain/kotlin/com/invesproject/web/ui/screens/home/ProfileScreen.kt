package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.AuthState
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun ProfileScreen(
    currentUser: User?,
    authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    val state by authViewModel.state.collectAsState()

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(24.px)
                maxWidth(600.px)
                margin(0.px, Auto.auto)
                width(100.percent)
                padding(16.px)
            }
        }
    ) {
        H2(
            attrs = {
                classes(WebTypography.HeadlineMedium)
                style {
                    color(WebColors.Primary)
                    margin(0.px)
                }
            }
        ) {
            Text("Profile")
        }

        if (currentUser != null) {
            Card(
                modifier = {
                    padding(24.px)
                }
            ) {
                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Column)
                            gap(16.px)
                        }
                    }
                ) {
                    if (isEditing) {
                        OutlinedInput(
                            value = name,
                            onValueChange = {
                                name = it
                                nameError = null
                            },
                            label = "Name",
                            isError = nameError != null,
                            errorMessage = nameError
                        )
                    } else {
                        ProfileField("Name", currentUser.name)
                    }

                    ProfileField("Email", currentUser.email)
                    ProfileField("Role", currentUser.role.name.lowercase().capitalize())

                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                justifyContent(if (isEditing) JustifyContent.SpaceBetween else JustifyContent.FlexEnd)
                                gap(8.px)
                                marginTop(8.px)
                            }
                        }
                    ) {
                        if (isEditing) {
                            Button(
                                attrs = {
                                    onClick {
                                        isEditing = false
                                        name = currentUser.name
                                    }
                                    style {
                                        backgroundColor(Color.transparent)
                                        color(WebColors.Primary)
                                        border(0.px)
                                        cursor("pointer")
                                        padding(8.px, 16.px)
                                    }
                                }
                            ) {
                                Text("Cancel")
                            }

                            PrimaryButton(
                                text = "Save",
                                onClick = {
                                    if (name.isBlank()) {
                                        nameError = "Name is required"
                                    } else {
                                        authViewModel.updateUserProfile(
                                            currentUser.copy(name = name)
                                        )
                                        isEditing = false
                                    }
                                }
                            )
                        } else {
                            PrimaryButton(
                                text = "Edit Profile",
                                onClick = { isEditing = true }
                            )
                        }
                    }
                }
            }

            Div(
                attrs = {
                    style {
                        marginTop(32.px)
                    }
                }
            ) {
                Button(
                    attrs = {
                        onClick { authViewModel.signOut() }
                        style {
                            backgroundColor(WebColors.Error)
                            color(WebColors.OnError)
                            border(0.px)
                            borderRadius(4.px)
                            padding(12.px, 24.px)
                            cursor("pointer")
                            fontSize(14.px)
                            width(100.percent)
                        }
                    }
                ) {
                    Text("Sign Out")
                }
            }
        }

        when (state) {
            is AuthState.Loading -> LoadingSpinner()
            is AuthState.Error -> ErrorMessage(
                message = (state as AuthState.Error).message,
                onRetry = {
                    if (isEditing && currentUser != null) {
                        authViewModel.updateUserProfile(currentUser.copy(name = name))
                    }
                }
            )
            else -> Unit
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(4.px)
            }
        }
    ) {
        Span(
            attrs = {
                style {
                    color(WebColors.OnSurfaceVariant)
                    fontSize(12.px)
                }
            }
        ) {
            Text(label)
        }
        Span(
            attrs = {
                style {
                    color(WebColors.OnSurface)
                    fontSize(16.px)
                }
            }
        ) {
            Text(value)
        }
    }
} 