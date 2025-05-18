package com.invesproject.android.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.AuthState
import com.invesproject.shared.presentation.viewmodel.AuthViewModel
import com.invesproject.shared.ui.components.ErrorMessage
import com.invesproject.shared.ui.components.LoadingSpinner
import com.invesproject.shared.ui.components.OutlinedInput
import com.invesproject.shared.ui.components.PrimaryButton
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: User?,
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            // Convert URI to ByteArray
            context.contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                // Upload the image
                authViewModel.updateProfilePicture(bytes)
            }
        }
    }

    val state by authViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { imagePicker.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (currentUser?.profilePictureUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(currentUser?.profilePictureUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Edit overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile Picture",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        currentUser?.let { user ->
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = user.role.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showEditDialog = true }
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.signOut()
                    onNavigateToLogin()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Sign Out")
            }
        }

        when (state) {
            is AuthState.Loading -> LoadingSpinner()
            is AuthState.Error -> ErrorMessage(
                message = (state as AuthState.Error).message,
                onRetry = {
                    if (showEditDialog && currentUser != null) {
                        authViewModel.updateUserProfile(currentUser.copy(name = editedName))
                    }
                }
            )
            else -> Unit
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile") },
            text = {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Name") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        currentUser?.let {
                            authViewModel.updateUserProfile(it.copy(name = editedName))
                        }
                        showEditDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
} 