package com.gih.campusone.ui.navigation

/**
 * Navigation routes for the CampusOne app
 * Uses sealed class for type-safe navigation
 */
sealed class Routes(val route: String) {

    // Auth Routes
    object Login : Routes("login")
    object SignUp : Routes("signup")

    // User Routes (Student/Professor)
    object Home : Routes("home")
    object Sos : Routes("sos")
    object CreateReport : Routes("create_report")
    object MyReports : Routes("my_reports")
    object Announcements : Routes("announcements")
    object AnnouncementDetail : Routes("announcement_detail/{announcementId}") {
        fun createRoute(announcementId: String) = "announcement_detail/$announcementId"
    }
    object ReportDetail : Routes("report_detail/{reportId}") {
        fun createRoute(reportId: String) = "report_detail/$reportId"
    }
    object About : Routes("about")

    // Admin Routes
    object AdminHome : Routes("admin_home")
    object AllReports : Routes("all_reports")
    object ManageAnnouncements : Routes("manage_announcements")

    // Navigation Graphs
    object AuthGraph : Routes("auth_graph")
    object UserGraph : Routes("user_graph")
    object AdminGraph : Routes("admin_graph")
}

