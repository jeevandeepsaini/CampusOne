package com.gih.campusone.ui.screens.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gih.campusone.data.model.ReportCategory
import com.gih.campusone.ui.navigation.Routes
import com.gih.campusone.ui.screens.auth.AuthState
import com.gih.campusone.ui.screens.auth.AuthViewModel

/**
 * Screen for creating and submitting campus issue reports
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    reportsViewModel: ReportsViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val uiState by reportsViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showCategoryMenu by remember { mutableStateOf(false) }

    // Extract user info from auth state
    val currentUser = when (val state = authState) {
        is AuthState.Authenticated -> Triple(state.uid, state.email, state.role)
        else -> Triple("", "", "")
    }

    // Show error snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            reportsViewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Report an Issue",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📝",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = "Submit Campus Issue",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Help us improve the campus by reporting issues",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Form Section
            Text(
                text = "Issue Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = showCategoryMenu,
                onExpandedChange = { showCategoryMenu = !showCategoryMenu }
            ) {
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category *") },
                    placeholder = { Text("Select issue category") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Category"
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu)
                    },
                    isError = uiState.categoryError != null,
                    supportingText = {
                        uiState.categoryError?.let { Text(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    enabled = !uiState.isLoading
                )

                ExposedDropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    ReportCategory.ALL.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Text(getCategoryIcon(category))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(category)
                                }
                            },
                            onClick = {
                                reportsViewModel.updateCategory(category)
                                showCategoryMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Field
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { reportsViewModel.updateDescription(it) },
                label = { Text("Description *") },
                placeholder = { Text("Describe the issue in detail...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Description"
                    )
                },
                isError = uiState.descriptionError != null,
                supportingText = {
                    Column {
                        uiState.descriptionError?.let { Text(it) }
                        Text(
                            "${uiState.description.length} characters",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                },
                minLines = 5,
                maxLines = 10,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Location Field (Optional)
            OutlinedTextField(
                value = uiState.location,
                onValueChange = { reportsViewModel.updateLocation(it) },
                label = { Text("Location (Optional)") },
                placeholder = { Text("e.g., Main Building, Room 101") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location"
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    val (uid, email, role) = currentUser
                    if (uid.isNotEmpty()) {
                        reportsViewModel.submitReport(
                            userId = uid,
                            userEmail = email,
                            userRole = role,
                            onSuccess = {
                                // Navigate to MyReportsScreen after successful submission
                                navController.navigate(Routes.MyReports.route) {
                                    popUpTo(Routes.CreateReport.route) { inclusive = true }
                                }
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submit Report",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Note",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Your report will be reviewed by campus administration\n" +
                                "• You can track the status in 'My Reports'\n" +
                                "• Please provide as much detail as possible\n" +
                                "• For emergencies, use the SOS feature instead",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Get emoji icon for category
 */
private fun getCategoryIcon(category: String): String {
    return when (category) {
        "Infrastructure" -> "🏗️"
        "Hygiene" -> "🧹"
        "Security" -> "🔒"
        "Network" -> "📡"
        "Other" -> "📌"
        else -> "📝"
    }
}


