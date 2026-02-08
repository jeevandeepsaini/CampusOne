package com.gih.campusone.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.gih.campusone.ui.screens.about.AboutScreen
import com.gih.campusone.ui.screens.admin.AdminHomeScreen
import com.gih.campusone.ui.screens.admin.AllReportsScreen
import com.gih.campusone.ui.screens.admin.ManageAnnouncementsScreen
import com.gih.campusone.ui.screens.announcements.AnnouncementDetailScreen
import com.gih.campusone.ui.screens.announcements.AnnouncementsScreen
import com.gih.campusone.ui.screens.auth.AuthState
import com.gih.campusone.ui.screens.auth.AuthViewModel
import com.gih.campusone.ui.screens.auth.LoginScreen
import com.gih.campusone.ui.screens.auth.SignUpScreen
import com.gih.campusone.ui.screens.home.HomeScreen
import com.gih.campusone.ui.screens.reports.CreateReportScreen
import com.gih.campusone.ui.screens.reports.MyReportsScreen
import com.gih.campusone.ui.screens.reports.ReportDetailScreen
import com.gih.campusone.ui.screens.sos.SosScreen

/**
 * Main navigation setup for CampusOne app
 * Handles navigation based on authentication state and user role
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    // Determine start destination based on auth state
    val startDestination = when (authState) {
        is AuthState.Loading -> Routes.AuthGraph.route
        is AuthState.Unauthenticated -> Routes.AuthGraph.route
        is AuthState.Authenticated -> {
            val authenticated = authState as AuthState.Authenticated
            if (authenticated.isAdmin) {
                Routes.AdminGraph.route
            } else {
                Routes.UserGraph.route
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Navigation Graph
        authNavGraph(navController, authViewModel)

        // User Navigation Graph (Student/Professor)
        userNavGraph(navController, authViewModel)

        // Admin Navigation Graph
        adminNavGraph(navController, authViewModel)
    }
}

/**
 * Authentication navigation graph
 * Contains Login and SignUp screens
 */
private fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = Routes.Login.route,
        route = Routes.AuthGraph.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

/**
 * User navigation graph (Student/Professor)
 * Contains all regular user screens
 */
private fun NavGraphBuilder.userNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = Routes.Home.route,
        route = Routes.UserGraph.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.Sos.route) {
            SosScreen(navController = navController)
        }

        composable(Routes.CreateReport.route) {
            CreateReportScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.MyReports.route) {
            MyReportsScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.Announcements.route) {
            AnnouncementsScreen(navController = navController)
        }

        composable(
            route = Routes.AnnouncementDetail.route,
            arguments = listOf(
                navArgument("announcementId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId") ?: ""
            AnnouncementDetailScreen(
                navController = navController,
                announcementId = announcementId
            )
        }

        composable(
            route = Routes.ReportDetail.route,
            arguments = listOf(
                navArgument("reportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            ReportDetailScreen(
                navController = navController,
                reportId = reportId
            )
        }

        composable(Routes.About.route) {
            AboutScreen(navController = navController)
        }
    }
}

/**
 * Admin navigation graph
 * Contains admin-specific screens
 */
private fun NavGraphBuilder.adminNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = Routes.AdminHome.route,
        route = Routes.AdminGraph.route
    ) {
        composable(Routes.AdminHome.route) {
            AdminHomeScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.AllReports.route) {
            AllReportsScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.ManageAnnouncements.route) {
            ManageAnnouncementsScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Admin can also access SOS
        composable(Routes.Sos.route) {
            SosScreen(navController = navController)
        }

        // Admin can view announcement details
        composable(
            route = Routes.AnnouncementDetail.route,
            arguments = listOf(
                navArgument("announcementId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId") ?: ""
            AnnouncementDetailScreen(
                navController = navController,
                announcementId = announcementId
            )
        }

        // Admin can view report details
        composable(
            route = Routes.ReportDetail.route,
            arguments = listOf(
                navArgument("reportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            ReportDetailScreen(
                navController = navController,
                reportId = reportId
            )
        }

        // Admin can access About
        composable(Routes.About.route) {
            AboutScreen(navController = navController)
        }
    }
}

