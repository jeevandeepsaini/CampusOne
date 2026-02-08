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
import com.gih.campusone.data.model.Report
import com.gih.campusone.data.model.ReportStatus
import com.gih.campusone.ui.navigation.Routes
import com.gih.campusone.ui.screens.auth.AuthViewModel
import com.gih.campusone.ui.screens.reports.ReportsViewModel
import com.gih.campusone.utils.toFormattedDate
import kotlinx.coroutines.launch

/**
 * Admin screen showing all reports with ability to update status
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllReportsScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    reportsViewModel: ReportsViewModel = viewModel()
) {
    // Collect all reports with lifecycle awareness for real-time updates
    val reports by reportsViewModel.getAllReports().collectAsStateWithLifecycle(initialValue = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Group reports by status for better organization
    val reportsByStatus = remember(reports) {
        reports.groupBy { it.status }
    }

    val pendingCount = reportsByStatus[ReportStatus.PENDING]?.size ?: 0
    val inProgressCount = reportsByStatus[ReportStatus.IN_PROGRESS]?.size ?: 0
    val resolvedCount = reportsByStatus[ReportStatus.RESOLVED]?.size ?: 0

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All Reports",
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
        if (reports.isEmpty()) {
            // Empty state
            EmptyReportsState(
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
                // Stats Card
                item {
                    StatsCard(
                        totalReports = reports.size,
                        pendingCount = pendingCount,
                        inProgressCount = inProgressCount,
                        resolvedCount = resolvedCount
                    )
                }

                // Header
                item {
                    Text(
                        text = "${reports.size} Total Report${if (reports.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Report items
                items(reports, key = { it.id }) { report ->
                    AdminReportCard(
                        report = report,
                        onStatusChange = { newStatus ->
                            reportsViewModel.updateReportStatus(
                                reportId = report.id,
                                newStatus = newStatus,
                                onSuccess = {
                                    // Show success snackbar
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Status updated to $newStatus",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                onError = { error ->
                                    // Show error snackbar
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = error,
                                            duration = SnackbarDuration.Long
                                        )
                                    }
                                }
                            )
                        },
                        onClick = {
                            navController.navigate(Routes.ReportDetail.createRoute(report.id))
                        }
                    )
                }
            }
        }
    }
}

/**
 * Stats card showing report counts by status
 */
@Composable
private fun StatsCard(
    totalReports: Int,
    pendingCount: Int,
    inProgressCount: Int,
    resolvedCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Report Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("⏳", "Pending", pendingCount)
                StatItem("🔄", "In Progress", inProgressCount)
                StatItem("✅", "Resolved", resolvedCount)
            }
        }
    }
}

/**
 * Individual stat item
 */
@Composable
private fun StatItem(icon: String, label: String, count: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Admin report card with status dropdown
 */
@Composable
private fun AdminReportCard(
    report: Report,
    onStatusChange: (String) -> Unit,
    onClick: () -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Category and Submitted By
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category with icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = getCategoryIcon(report.category),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = report.category,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "by ${report.createdByEmail.substringBefore("@")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // User role badge
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = report.createdByRole.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description preview
            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Location (if available)
            if (report.location.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = report.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Date and Status Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = report.createdAt.toFormattedDate(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Status Dropdown
                Box {
                    OutlinedButton(
                        onClick = { showStatusMenu = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = getStatusColor(report.status).copy(alpha = 0.1f),
                            contentColor = getStatusContentColor(report.status)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = getStatusIcon(report.status),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = report.status,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Change Status",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        ReportStatus.ALL.forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = getStatusIcon(status),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = status)
                                    }
                                },
                                onClick = {
                                    showStatusMenu = false
                                    if (status != report.status) {
                                        onStatusChange(status)
                                    }
                                },
                                leadingIcon = if (status == report.status) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Empty state when no reports exist
 */
@Composable
private fun EmptyReportsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📋",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Reports Submitted",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "When users submit reports, they will appear here for review and status updates.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

/**
 * Get status icon emoji
 */
private fun getStatusIcon(status: String): String {
    return when (status) {
        ReportStatus.PENDING -> "⏳"
        ReportStatus.IN_PROGRESS -> "🔄"
        ReportStatus.RESOLVED -> "✅"
        else -> "📋"
    }
}

/**
 * Get status color
 */
@Composable
private fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status) {
        ReportStatus.PENDING -> MaterialTheme.colorScheme.error
        ReportStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        ReportStatus.RESOLVED -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

/**
 * Get status content color
 */
@Composable
private fun getStatusContentColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status) {
        ReportStatus.PENDING -> MaterialTheme.colorScheme.onError
        ReportStatus.IN_PROGRESS -> MaterialTheme.colorScheme.onPrimary
        ReportStatus.RESOLVED -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

/**
 * Get category icon emoji
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

