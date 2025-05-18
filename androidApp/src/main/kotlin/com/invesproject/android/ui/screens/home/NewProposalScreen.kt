package com.invesproject.android.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    proposalViewModel: ProposalViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf<BusinessSector?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var fileBytes by remember { mutableStateOf<ByteArray?>(null) }
    var showSectorMenu by remember { mutableStateOf(false) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var budgetError by remember { mutableStateOf<String?>(null) }
    var sectorError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            context.contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                fileBytes = bytes
                // Get file name from URI
                context.contentResolver.query(selectedUri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val fileName = cursor.getString(nameIndex)
                    selectedFileName = fileName
                    // Create proposal with file
                    if (title.isNotBlank() && description.isNotBlank() && budget.isNotBlank() && currentUser != null) {
                        proposalViewModel.createProposalWithFile(
                            title = title,
                            description = description,
                            estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
                            sector = selectedSector!!,
                            innovatorId = currentUser.id,
                            fileBytes = bytes,
                            fileName = fileName
                        )
                    }
                }
            }
        }
    }

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

        Box {
            OutlinedButton(
                onClick = { showSectorMenu = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sector: ${selectedSector?.name ?: "Select Sector"}")
            }

            DropdownMenu(
                expanded = showSectorMenu,
                onDismissRequest = { showSectorMenu = false }
            ) {
                BusinessSector.values().forEach { sector ->
                    DropdownMenuItem(
                        text = { Text(sector.name) },
                        onClick = {
                            selectedSector = sector
                            showSectorMenu = false
                        }
                    )
                }
            }
        }
        if (sectorError != null) {
            Text(
                text = sectorError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Business Plan Upload Section
        OutlinedButton(
            onClick = { filePicker.launch("application/pdf") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AttachFile, contentDescription = "Attach File")
                Text(selectedFileName ?: "Upload Business Plan (PDF)")
            }
        }

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
                    val currentFileName = selectedFileName
                    val currentFileBytes = fileBytes
                    
                    if (currentFileName != null && currentFileBytes != null) {
                        proposalViewModel.createProposalWithFile(
                            title = title,
                            description = description,
                            estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
                            sector = selectedSector!!,
                            innovatorId = currentUser.id,
                            fileBytes = currentFileBytes,
                            fileName = currentFileName
                        )
                    } else {
                        proposalViewModel.createProposal(
                            title = title,
                            description = description,
                            estimatedBudget = budget.toDoubleOrNull() ?: 0.0,
                            sector = selectedSector!!,
                            innovatorId = currentUser.id
                        )
                    }
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
                    if (selectedFileName != null && fileBytes != null) {
                        filePicker.launch("application/pdf")
                    } else {
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
                }
            )
            is ProposalState.Success -> {
                LaunchedEffect(Unit) {
                    title = ""
                    description = ""
                    budget = ""
                    selectedSector = null
                    selectedFileName = null
                    fileBytes = null
                    onNavigateBack()
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