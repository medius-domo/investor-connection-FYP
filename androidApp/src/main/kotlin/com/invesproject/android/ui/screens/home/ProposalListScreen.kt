package com.invesproject.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.invesproject.android.data.AndroidProposalViewModel
import com.invesproject.shared.domain.model.BusinessProposal
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.presentation.viewmodel.MessageViewModel
import com.invesproject.shared.presentation.viewmodel.ProposalState
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.shared.ui.components.ErrorMessage
import com.invesproject.shared.ui.components.LoadingSpinner
import com.invesproject.shared.ui.components.ProposalCard
import com.invesproject.shared.ui.components.SectorDropdown
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalListScreen(
    currentUser: User?,
    proposalViewModel: AndroidProposalViewModel,
    messageViewModel: MessageViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedSector by remember { mutableStateOf<BusinessSector?>(null) }
    var showContactDialog by remember { mutableStateOf(false) }
    var selectedProposalId by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }

    val proposals by proposalViewModel.proposals.collectAsState()
    val state by proposalViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (currentUser?.role == UserRole.INNOVATOR) {
            proposalViewModel.getProposalsByInnovator(currentUser.id)
        }
        // No need to call getAllProposals() as it's automatically observed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (currentUser?.role == UserRole.INVESTOR) {
            SectorDropdown(
                selectedSector = selectedSector?.name ?: "All Sectors",
                onSectorSelected = { sector ->
                    selectedSector = BusinessSector.valueOf(sector)
                    if (selectedSector != null) {
                        proposalViewModel.getProposalsBySector(selectedSector!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (state) {
            is ProposalState.Loading -> LoadingSpinner()
            is ProposalState.Error -> ErrorMessage(
                message = (state as ProposalState.Error).message,
                onRetry = {
                    if (selectedSector != null) {
                        proposalViewModel.getProposalsBySector(selectedSector!!)
                    } else if (currentUser?.role == UserRole.INNOVATOR) {
                        proposalViewModel.getProposalsByInnovator(currentUser.id)
                    }
                }
            )
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(proposals) { proposal ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = proposal.title,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = proposal.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Budget: $${proposal.estimatedBudget}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Sector: ${proposal.sector.name}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (currentUser?.role == UserRole.INVESTOR && proposal.businessPlanUrl != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    proposalViewModel.downloadAndOpenBusinessPlan(context, proposal)
                                                } catch (e: Exception) {
                                                    // Handle error
                                                }
                                            }
                                        },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Download,
                                                contentDescription = "Download"
                                            )
                                            Text("View Business Plan")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showContactDialog && selectedProposalId != null) {
        AlertDialog(
            onDismissRequest = {
                showContactDialog = false
                selectedProposalId = null
                messageText = ""
            },
            title = { Text("Contact Innovator") },
            text = {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (messageText.isNotBlank() && currentUser != null) {
                            val proposal = proposals.find { it.id == selectedProposalId }
                            if (proposal != null) {
                                messageViewModel.sendMessage(
                                    senderId = currentUser.id,
                                    receiverId = proposal.innovatorId,
                                    content = messageText,
                                    proposalId = proposal.id
                                )
                                proposalViewModel.markInterest(proposal.id, currentUser.id)
                            }
                        }
                        showContactDialog = false
                        selectedProposalId = null
                        messageText = ""
                    }
                ) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showContactDialog = false
                        selectedProposalId = null
                        messageText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
} 