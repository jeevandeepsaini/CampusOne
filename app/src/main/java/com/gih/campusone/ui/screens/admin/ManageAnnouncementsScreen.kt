package com.gih.campusone.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gih.campusone.data.model.Announcement
import com.gih.campusone.ui.screens.announcements.AnnouncementsViewModel
import com.gih.campusone.ui.screens.auth.AuthState
import com.gih.campusone.ui.screens.auth.AuthViewModel
import com.gih.campusone.utils.toFormattedDate
import kotlinx.coroutines.launch

/**
 * Admin screen for managing announcements (create and delete)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAnnouncementsScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    announcementsViewModel: AnnouncementsViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val announcements by announcementsViewModel.getAnnouncements()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val uiState by announcementsViewModel.uiState.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Announcement?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Get current user ID
    val userId = when (val state = authState) {
        is AuthState.Authenticated -> state.uid
        else -> ""
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Announcements",
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
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Announcement"
                )
            }
        }
    ) { paddingValues ->
        if (announcements.isEmpty()) {
            // Empty state
            EmptyAnnouncementsState(
                onAddClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                item {
                    Text(
                        text = "${announcements.size} Announcement${if (announcements.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Announcement items
                items(announcements, key = { it.id }) { announcement ->
                    ManageAnnouncementCard(
                        announcement = announcement,
                        onDeleteClick = { showDeleteDialog = announcement }
                    )
                }
            }
        }
    }

    // Add Announcement Dialog
    if (showAddDialog) {
        AddAnnouncementDialog(
            uiState = uiState,
            onTitleChange = { announcementsViewModel.updateTitle(it) },
            onMessageChange = { announcementsViewModel.updateMessage(it) },
            onDismiss = {
                showAddDialog = false
                announcementsViewModel.clearError()
            },
            onConfirm = {
                announcementsViewModel.addAnnouncement(
                    title = uiState.title,
                    message = uiState.message,
                    createdByUid = userId,
                    onSuccess = {
                        showAddDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Announcement created successfully",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onError = { error ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = error,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                )
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { announcement ->
        DeleteConfirmationDialog(
            announcementTitle = announcement.title,
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                announcementsViewModel.deleteAnnouncement(
                    announcementId = announcement.id,
                    onSuccess = {
                        showDeleteDialog = null
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Announcement deleted",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onError = { error ->
                        showDeleteDialog = null
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = error,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                )
            }
        )
    }
}

/**
 * Empty state for announcements management
 */
@Composable
private fun EmptyAnnouncementsState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📢",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Announcements",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create your first campus announcement to share important news and updates.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create Announcement")
        }
    }
}

/**
 * Card showing announcement with delete action
 */
@Composable
private fun ManageAnnouncementCard(
    announcement: Announcement,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Icon and Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = announcement.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Delete button
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Message preview
            Text(
                text = announcement.message,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Posted ${announcement.createdAt.toFormattedDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Dialog for adding new announcement
 */
@Composable
private fun AddAnnouncementDialog(
    uiState: com.gih.campusone.ui.screens.announcements.AnnouncementUiState,
    onTitleChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text("Create Announcement")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    label = { Text("Title *") },
                    placeholder = { Text("Announcement title") },
                    isError = uiState.titleError != null,
                    supportingText = {
                        uiState.titleError?.let { Text(it) }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.message,
                    onValueChange = onMessageChange,
                    label = { Text("Message *") },
                    placeholder = { Text("Announcement message") },
                    isError = uiState.messageError != null,
                    supportingText = {
                        uiState.messageError?.let { Text(it) }
                    },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !uiState.isLoading
            ) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Dialog for delete confirmation
 */
@Composable
private fun DeleteConfirmationDialog(
    announcementTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text("Delete Announcement?")
        },
        text = {
            Text(
                "Are you sure you want to delete \"$announcementTitle\"? This action cannot be undone."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
