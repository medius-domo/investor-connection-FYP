package com.invesproject.web.ui.screens.home

import androidx.compose.runtime.*
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.MessageState
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.web.ui.components.*
import com.invesproject.web.ui.theme.WebColors
import com.invesproject.web.ui.theme.WebTypography
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
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

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(16.px)
                maxWidth(800.px)
                margin(0.px, Auto.auto)
                width(100.percent)
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
            Text("Messages")
        }

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
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                justifyContent(JustifyContent.Center)
                                alignItems(AlignItems.Center)
                                padding(32.px)
                            }
                        }
                    ) {
                        Text("No messages yet")
                    }
                } else {
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                flexDirection(FlexDirection.Column)
                                gap(8.px)
                            }
                        }
                    ) {
                        messages.sortedByDescending { it.timestamp }.forEach { message ->
                            val isCurrentUser = message.senderId == currentUser?.id
                            MessageBubble(message, isCurrentUser, dateFormat)

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

@Composable
private fun MessageBubble(
    message: com.invesproject.shared.domain.model.Message,
    isCurrentUser: Boolean,
    dateFormat: SimpleDateFormat
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(if (isCurrentUser) AlignItems.FlexEnd else AlignItems.FlexStart)
                width(100.percent)
            }
        }
    ) {
        Div(
            attrs = {
                style {
                    backgroundColor(if (isCurrentUser) WebColors.Primary else WebColors.Surface)
                    color(if (isCurrentUser) WebColors.OnPrimary else WebColors.OnSurface)
                    padding(12.px, 16.px)
                    borderRadius(8.px)
                    maxWidth(70.percent)
                    boxShadow("0 1px 2px rgba(0,0,0,0.1)")
                    property("word-break", "break-word")
                }
            }
        ) {
            P(
                attrs = {
                    style {
                        margin(0.px)
                        marginBottom(4.px)
                    }
                }
            ) {
                Text(message.content)
            }
            Span(
                attrs = {
                    style {
                        fontSize(12.px)
                        opacity(0.8)
                    }
                }
            ) {
                Text(dateFormat.format(Date(message.timestamp)))
            }
        }
    }
} 