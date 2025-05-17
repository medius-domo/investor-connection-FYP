package com.invesproject.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.MessageState
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.shared.ui.components.ErrorMessage
import com.invesproject.shared.ui.components.LoadingSpinner
import com.invesproject.shared.ui.components.MessageBubble
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageListScreen(
    currentUser: User?,
    messageViewModel: MessageViewModel
) {
    val messages by messageViewModel.messages.collectAsState()
    val state by messageViewModel.state.collectAsState()
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            messageViewModel.getMessagesForUser(currentUser.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (state) {
            is MessageState.Loading -> LoadingSpinner()
            is MessageState.Error -> ErrorMessage(
                message = (state as MessageState.Error).message,
                onRetry = {
                    if (currentUser != null) {
                        messageViewModel.getMessagesForUser(currentUser.id)
                    }
                }
            )
            else -> {
                if (messages.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "No messages yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages.sortedByDescending { it.timestamp }) { message ->
                            MessageBubble(
                                message = message.content,
                                isCurrentUser = message.senderId == currentUser?.id,
                                timestamp = dateFormat.format(Date(message.timestamp))
                            )

                            // Mark message as read if it's received and unread
                            if (!message.isRead && message.receiverId == currentUser?.id) {
                                LaunchedEffect(message.id) {
                                    messageViewModel.markMessageAsRead(message.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 