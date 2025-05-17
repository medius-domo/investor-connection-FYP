package com.invesproject.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.AuthState
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.shared.ui.components.ErrorMessage
import com.invesproject.shared.ui.components.LoadingSpinner
import com.invesproject.shared.ui.components.OutlinedInput
import com.invesproject.shared.ui.components.PrimaryButton

@Composable
fun ProfileScreen(
    currentUser: User?,
    authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    val state by authViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (currentUser != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
                        Text(
                            text = "Name: ${currentUser.name}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Email: ${currentUser.email}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Role: ${currentUser.role.name.lowercase().capitalize()}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isEditing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    isEditing = false
                                    name = currentUser.name
                                }
                            ) {
                                Text("Cancel")
                            }
                            Button(
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
                            ) {
                                Text("Save")
                            }
                        }
                    } else {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Edit Profile")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Sign Out",
                onClick = { authViewModel.signOut() }
            )
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