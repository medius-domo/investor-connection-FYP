package com.invesproject.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.invesproject.shared.domain.model.BusinessSector
import com.invesproject.shared.domain.model.User
import com.invesproject.shared.presentation.viewmodel.ProposalState
import com.invesproject.shared.presentation.viewmodel.ProposalViewModel
import com.invesproject.shared.ui.components.ErrorMessage
import com.invesproject.shared.ui.components.LoadingSpinner
import com.invesproject.shared.ui.components.OutlinedInput
import com.invesproject.shared.ui.components.SectorDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProposalScreen(
    currentUser: User?,
    proposalViewModel: ProposalViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf<BusinessSector?>(null) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var budgetError by remember { mutableStateOf<String?>(null) }
    var sectorError by remember { mutableStateOf<String?>(null) }

    val state by proposalViewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Business Proposal",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedInput(
            value = title,
            onValueChange = {
                title = it
                titleError = null
            },
            label = "Title",
            isError = titleError != null,
            errorMessage = titleError
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                descriptionError = null
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 6,
            isError = descriptionError != null
        )
        if (descriptionError != null) {
            Text(
                text = descriptionError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = budget,
            onValueChange = {
                budget = it.filter { char -> char.isDigit() || char == '.' }
                budgetError = null
            },
            label = { Text("Estimated Budget ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            isError = budgetError != null
        )
        if (budgetError != null) {
            Text(
                text = budgetError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectorDropdown(
            selectedSector = selectedSector?.name ?: "Select Sector",
            onSectorSelected = { sector ->
                selectedSector = BusinessSector.valueOf(sector)
                sectorError = null
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (sectorError != null) {
            Text(
                text = sectorError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                var hasError = false
                if (title.isBlank()) {
                    titleError = "Title is required"
                    hasError = true
                }
                if (description.isBlank()) {
                    descriptionError = "Description is required"
                    hasError = true
                }
                if (budget.isBlank()) {
                    budgetError = "Budget is required"
                    hasError = true
                }
                if (selectedSector == null) {
                    sectorError = "Please select a sector"
                    hasError = true
                }

                if (!hasError && currentUser != null) {
                    proposalViewModel.createProposal(
                        title = title,
                        description = description,
                        estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
                        sector = selectedSector!!,
                        innovatorId = currentUser.id
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Proposal")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ProposalState.Loading -> LoadingSpinner()
            is ProposalState.Error -> ErrorMessage(
                message = (state as ProposalState.Error).message,
                onRetry = {
                    if (currentUser != null && selectedSector != null) {
                        proposalViewModel.createProposal(
                            title = title,
                            description = description,
                            estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
                            sector = selectedSector!!,
                            innovatorId = currentUser.id
                        )
                    }
                }
            )
            is ProposalState.Success -> {
                LaunchedEffect(Unit) {
                    title = ""
                    description = ""
                    budget = ""
                    selectedSector = null
                }
                Text(
                    text = "Proposal submitted successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> Unit
        }
    }
} 